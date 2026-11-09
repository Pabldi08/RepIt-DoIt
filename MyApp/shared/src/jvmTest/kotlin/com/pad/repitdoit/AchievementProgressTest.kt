package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.features.achievement.data.repository.AchievementsRepositoryImpl
import com.pad.repitdoit.features.achievement.domain.usecase.GetAllowedAchievementsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Properties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Clock

class AchievementProgressTest {
    @Test
    fun readsProgressForEligibleAchievementsOnly() = runBlocking {
        withDatabase { db, _ ->
            val now = Clock.System.now().toEpochMilliseconds()
            val hour = 3_600_000L
            val user = LOCAL_TEST_USER_ID
            val push = "test-workout-001"
            val cardio = "test-workout-002"
            val bench = "test-ex-001"
            val press = "test-ex-002"
            val useCase = DefaultAppContainer(db, Dispatchers.Unconfined).getAllowedAchievementsUseCase

            testDb(db).seed()
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.rewardQueries.insertReward("locked", "LOCKED", "Locked", "KEY", null, null)
            db.addAchievement("workouts", "WORKOUT_COUNT", workout = push)
            db.addAchievement("sets", "EXERCISE_SETS", exercise = bench, workout = push)
            db.addAchievement("reps", "EXERCISE_REPS", exercise = bench, days = 1)
            db.addAchievement("zero", "EXERCISE_REPS", exercise = press, workout = cardio)
            db.addAchievement("locked", "WORKOUT_COUNT", reward = "locked")
            db.addAchievement("unsupported", "WORKOUT_COUNT", operator = "<")
            db.addAchievement("combined", "WORKOUT_COUNT")
            db.achievementConditionQueries.insertAchievementCondition(
                id = "second-condition", achievement_id = "combined",
                condition_type = "EXERCISE_SETS", comparison_operator = ">=", target_value = 1.0,
                exercise_id = null, workout_id = null, period_days = null, workout_type = null
            )

            assertEquals(emptyList(), useCase())
            db.userRewardQueries.insertUserReward("grant", user, "key", now - 48 * hour)
            assertEquals(0L, useCase().first { it.id == "workouts" }.progress)

            db.addSession("before-key", user, push, now - 72 * hour, bench, 100)
            db.addSession("outside-period", user, push, now - 36 * hour, bench, 20)
            db.addSession("recent", user, push, now - hour, bench, 8)
            db.addSession("other-exercise", user, push, now - hour, press, 11)
            db.addSession("other-workout", user, cardio, now - hour, bench, 5)
            db.addSession("aborted", user, push, now - hour, bench, 100, status = "ABORTED")
            db.addSession("active", user, push, now - hour, bench, 100, status = "ACTIVE")
            db.addSession("unfinished-set", user, push, now - hour, bench, 100, setCompleted = false)

            db.userQueries.insertUser("other", "Other", "Other user", null)
            db.workoutQueries.insertWorkout(
                "other-workout", "other", "Other workout", null, null, null, now, now
            )
            db.addSession("other-user", "other", "other-workout", now - hour, bench, 100)

            val achievements = useCase().associateBy { it.id }
            assertEquals(setOf("workouts", "sets", "reps", "zero", "unsupported", "combined"), achievements.keys)
            assertEquals(4L, achievements.getValue("workouts").progress)
            assertEquals(2L, achievements.getValue("sets").progress)
            assertEquals(13L, achievements.getValue("reps").progress)
            assertEquals(2.5, achievements.getValue("reps").achivementGoal)
            assertEquals(0L, achievements.getValue("zero").progress)
            for (id in listOf("unsupported", "combined")) {
                assertNull(achievements.getValue(id).progress)
                assertNull(achievements.getValue(id).achivementGoal)
            }
        }
    }

    @Test
    fun migratesExistingConditionAndEnforcesWorkoutReference() = runBlocking {
        withDatabase { db, driver ->
            testDb(db).seed()
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.addAchievement("legacy", "WORKOUT_COUNT", days = 7)
            removeVersionThreeSchema(driver)
            driver.execute(null, "ALTER TABLE AchievementCondition DROP COLUMN workout_id", 0)

            RepItDoItDatabase.Schema.migrate(driver, 1, RepItDoItDatabase.Schema.version)
            val migrated = db.achievementConditionQueries.selectAchievementConditionById("legacy").executeAsOne()
            assertEquals(7L, migrated.period_days)
            assertNull(migrated.workout_id)

            db.achievementConditionQueries.updateAchievementCondition(
                condition_type = migrated.condition_type, comparison_operator = migrated.comparison_operator,
                target_value = migrated.target_value, exercise_id = null, workout_id = "test-workout-001",
                period_days = migrated.period_days, workout_type = null, id = migrated.id, achievement_id = migrated.achievement_id
            )
            val updated = db.achievementConditionQueries.selectAchievementConditionById("legacy").executeAsOne()
            assertEquals("test-workout-001", updated.workout_id)
            assertFailsWith<java.sql.SQLException> {
                db.addAchievement("invalid", "WORKOUT_COUNT", workout = "missing")
            }
        }
    }

    @Test
    fun migratesVersionOneWithExistingWorkoutColumn() = runBlocking {
        withDatabase { db, driver ->
            testDb(db).seed()
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.addAchievement("legacy", "WORKOUT_COUNT")
            removeVersionThreeSchema(driver)
            driver.execute(null, "ALTER TABLE AchievementCondition DROP COLUMN workout_id", 0)
            driver.execute(null, "ALTER TABLE AchievementCondition ADD COLUMN workout_id TEXT", 0)
            driver.execute(
                null, "UPDATE AchievementCondition SET workout_id = 'test-workout-001' WHERE id = 'legacy'", 0
            )

            RepItDoItDatabase.Schema.migrate(driver, 1, RepItDoItDatabase.Schema.version)
            val migrated = db.achievementConditionQueries.selectAchievementConditionById("legacy").executeAsOne()
            assertEquals("test-workout-001", migrated.workout_id)
            assertFailsWith<java.sql.SQLException> {
                db.addAchievement("invalid", "WORKOUT_COUNT", workout = "missing")
            }
        }
    }

    @Test
    fun countsHistoricalTypesForEachUserAndCombinesFilters() = runBlocking {
        withDatabase { db, _ ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val other = "other"
            val push = "test-workout-001"
            val cardio = "test-workout-002"
            val bench = "test-ex-001"
            val press = "test-ex-002"
            val time = Clock.System.now().toEpochMilliseconds() - 60_000
            db.userQueries.insertUser(other, "Other", "Other user", null)
            db.workoutQueries.insertWorkout("other-push", other, "Pecho", null, null, null, time, time)
            db.rewardQueries.insertReward("badge", "BADGE", "Badge", "BADGE", null, null)
            for (owner in listOf(user, other)) {
                db.userRewardQueries.insertUserReward("grant-$owner", owner, "badge", time - 100)
            }
            db.addAchievement("chest", "WORKOUT_COUNT", reward = "badge", workoutType = "CHEST")
            db.addAchievement("triceps", "WORKOUT_COUNT", reward = "badge", workoutType = "TRICEPS")
            db.addAchievement("back", "WORKOUT_COUNT", reward = "badge", workoutType = "BACK")
            db.addAchievement("general", "WORKOUT_COUNT", reward = "badge")
            db.addAchievement("sets", "EXERCISE_SETS", bench, push, reward = "badge", workoutType = "CHEST")
            db.addAchievement("reps", "EXERCISE_REPS", bench, reward = "badge", workoutType = "CHEST")
            db.addAchievement("wrong-workout", "EXERCISE_REPS", bench, cardio, reward = "badge", workoutType = "CHEST")

            val types = db.workoutTypeAssignmentQueries
            types.assignWorkoutType(workoutId = push, workoutType = "CHEST", userId = user)
            types.assignWorkoutType(workoutId = push, workoutType = "TRICEPS", userId = user)
            types.assignWorkoutType(workoutId = push, workoutType = "CHEST", userId = user)
            types.assignWorkoutType(workoutId = push, workoutType = "BACK", userId = other)
            types.removeWorkoutType(workoutId = push, workoutType = "TRICEPS", userId = other)
            types.assignWorkoutType(workoutId = "other-push", workoutType = "CHEST", userId = other)
            assertEquals(listOf("CHEST", "TRICEPS"), types.selectWorkoutTypes(push, user).executeAsList())
            assertEquals(emptyList(), types.selectWorkoutTypes(push, other).executeAsList())

            db.addSession("mixed", user, push, time, bench, 8, status = "ACTIVE")
            db.addSession("mixed-press", user, push, time, press, 11)
            db.addSession("unclassified", user, cardio, time, bench, 17)
            db.addSession("other-chest", other, "other-push", time, bench, 3)
            db.addSession("cancelled", user, push, time, bench, 99, status = "ABORTED")
            db.addSession("unfinished", user, push, time, bench, 99, setCompleted = false)

            types.removeWorkoutType(workoutId = push, workoutType = "CHEST", userId = user)
            types.assignWorkoutType(workoutId = push, workoutType = "BACK", userId = user)
            db.workoutSessionQueries.updateWorkoutSession(
                finished_at = time, status = "COMPLETED", notes = null, id = "mixed", user_id = user
            )
            db.addSession("reclassified", user, push, time, bench, 10)

            assertEquals(
                mapOf("chest" to 3L, "triceps" to 4L, "back" to 1L, "general" to 5L,
                    "sets" to 1L, "reps" to 8L, "wrong-workout" to 0L),
                DefaultAppContainer(db, Dispatchers.Unconfined).getAllowedAchievementsUseCase()
                    .associate { it.id to it.progress }
            )
            val otherUseCase = GetAllowedAchievementsUseCase(
                AchievementsRepositoryImpl(db, Dispatchers.Unconfined, other)
            )
            assertEquals(
                mapOf("chest" to 1L, "triceps" to 0L, "back" to 0L, "general" to 1L,
                    "sets" to 0L, "reps" to 3L, "wrong-workout" to 0L),
                otherUseCase().associate { it.id to it.progress }
            )
        }
    }

    @Test
    fun migratingVersionTwoPreservesUntypedHistory() = runBlocking {
        withDatabase { db, driver ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val workout = "test-workout-001"
            val time = Clock.System.now().toEpochMilliseconds() - 60_000
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.userRewardQueries.insertUserReward("grant", user, "key", time - 100)
            db.addAchievement("general", "EXERCISE_REPS", workout = workout)
            db.userAchievementQueries.insertUserAchievement("unlocked", user, "general", time)
            db.addSession("legacy", user, workout, time, "test-ex-001", 8)
            removeVersionThreeSchema(driver)

            RepItDoItDatabase.Schema.migrate(driver, 2, RepItDoItDatabase.Schema.version)
            db.workoutTypeAssignmentQueries.assignWorkoutType(workoutId = workout, workoutType = "CHEST", userId = user)
            db.addAchievement("chest", "EXERCISE_REPS", workoutType = "CHEST")

            val useCase = DefaultAppContainer(db, Dispatchers.Unconfined).getAllowedAchievementsUseCase
            assertEquals(mapOf("general" to 8L, "chest" to 0L), useCase().associate { it.id to it.progress })
            assertEquals("general", db.userAchievementQueries.selectAchievementsByUser(user).executeAsOne().achievement_id)
        }
    }

    private fun removeVersionThreeSchema(driver: JdbcSqliteDriver) {
        driver.execute(null, "DROP TABLE WorkoutSessionType", 0)
        driver.execute(null, "DROP TABLE WorkoutTypeAssignment", 0)
        driver.execute(null, "ALTER TABLE AchievementCondition DROP COLUMN workout_type", 0)
    }

    @Test
    fun fakeContainerHasNoLocalAchievements() = runBlocking {
        assertEquals(emptyList(), DefaultAppContainer().getAllowedAchievementsUseCase())
    }

    private suspend fun withDatabase(block: suspend (RepItDoItDatabase, JdbcSqliteDriver) -> Unit) {
        val properties = Properties().apply { setProperty("foreign_keys", "true") }
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, properties).use { driver ->
            RepItDoItDatabase.Schema.create(driver)
            block(RepItDoItDatabase(driver), driver)
        }
    }

    private fun RepItDoItDatabase.addAchievement(
        id: String,
        type: String,
        exercise: String? = null,
        workout: String? = null,
        days: Long? = null,
        reward: String = "key",
        operator: String = ">=",
        workoutType: String? = null
    ) {
        achievementQueries.insertAchievement(id, reward, id, id, null, "ALL", null)
        achievementConditionQueries.insertAchievementCondition(
            id = id, achievement_id = id, condition_type = type, comparison_operator = operator,
            target_value = 2.5, exercise_id = exercise, workout_id = workout, period_days = days,
            workout_type = workoutType
        )
    }

    private fun RepItDoItDatabase.addSession(
        id: String,
        user: String,
        workout: String,
        timestamp: Long,
        exercise: String,
        reps: Long,
        status: String = "COMPLETED",
        setCompleted: Boolean = true
    ) {
        workoutSessionQueries.insertWorkoutSession(
            id = id, user_id = user, workout_id = workout, workout_name_snapshot = workout,
            workout_description_snapshot = null, started_at = timestamp - 1,
            finished_at = timestamp.takeUnless { status == "ACTIVE" }, status = status, notes = null
        )
        sessionExerciseQueries.insertSessionExercise(
            id = id, session_id = id, exercise_id = exercise, exercise_name_snapshot = exercise,
            exercise_description_snapshot = null, position = 0, notes = null, user_id = user
        )
        sessionSetQueries.insertSessionSet(
            id = id, session_exercise_id = id, set_number = 1, weight_kg = null, reps = reps,
            duration_seconds = null, completed_at = timestamp.takeIf { setCompleted }, user_id = user
        )
    }
}
