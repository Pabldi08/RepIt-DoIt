package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.features.gamification.data.repository.GamificationRepositoryImpl
import com.pad.repitdoit.features.gamification.domain.usecase.GetAllowedAchievementsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Properties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Clock

// Comprueba el cálculo del progreso y la obtención de logros.
class AchievementProgressTest {
    @Test
    fun awardsCompletedAchievementsAndChainedRewardsOnlyOnce() = runBlocking {
        withDatabase { db, _ ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val timestamp = Clock.System.now().toEpochMilliseconds() - 1000
            val container = DefaultAppContainer(db, Dispatchers.Unconfined)
            container.loginAsDemoUser()
            val startingXp = container.getTotalXpUseCase()
            val initialKey = "KEY_LEVEL_0"
            db.rewardQueries.insertReward("next", "NEXT_KEY", "Next key", "KEY", null, null)
            db.rewardQueries.insertReward("prize", "PRIZE", "Prize", "COLLECTIBLE", null, null)
            db.addAchievement("first", "WORKOUT_COUNT", reward = initialKey)
            db.addAchievement("second", "WORKOUT_COUNT", reward = "next")
            db.addAchievement("unsupported", "WORKOUT_COUNT", reward = initialKey, operator = "<")
            db.achievementQueries.updateAchievementXpReward(30, "first")
            db.achievementQueries.updateAchievementXpReward(20, "second")
            db.achievementRewardQueries.insertAchievementReward("first", "next")
            db.achievementRewardQueries.insertAchievementReward("first", "prize")
            db.achievementRewardQueries.insertAchievementReward("second", "prize")
            db.userRewardQueries.insertUserReward("existing", user, "prize", timestamp)

            container.awardCompletedAchievementsUseCase()
            assertEquals(startingXp, container.getTotalXpUseCase())

            val workout = requireNotNull(container.getWorkoutUseCase("test-workout-001"))
            repeat(3) { index ->
                container.completeWorkoutSessionUseCase("completed-$index", workout, timestamp - 1, timestamp)
            }
            val grants = db.userAchievementQueries.selectAchievementsByUser(user).executeAsList()
            assertEquals(setOf("first", "second"), grants.map { it.achievement_id }.toSet())
            assertEquals(startingXp + 50, container.getTotalXpUseCase())
            assertEquals(timestamp, container.getRewardsUseCase().single().obtainedAt)
            assertEquals(setOf(initialKey, "next", "prize"), db.userRewardQueries
                .selectRewardsByUser(user).executeAsList().map { it.reward_id }.toSet())

            container.completeWorkoutSessionUseCase("completed-2", workout, timestamp - 1, timestamp)
            container.awardCompletedAchievementsUseCase()
            assertEquals(3, db.workoutSessionQueries.selectWorkoutSessionsByUser(user).executeAsList().size)
            assertEquals(grants, db.userAchievementQueries.selectAchievementsByUser(user).executeAsList())
            assertEquals(startingXp + 50, container.getTotalXpUseCase())
        }
    }

    @Test
    fun readsProgressForEligibleAchievementsOnly() = runBlocking {
        withDatabase { db, _ ->
            val now = Clock.System.now().toEpochMilliseconds()
            val hour = 3_600_000L
            val user = LOCAL_TEST_USER_ID
            val push = "test-workout-001"
            val cardio = "test-workout-002"
            val bench = "908d354a-8282-5726-9115-9f322536e428"
            val press = "05997e70-373b-5097-8be8-2037ea2b82f0"
            testDb(db).seed()
            val container = DefaultAppContainer(db, Dispatchers.Unconfined)
            container.loginAsDemoUser()
            val useCase = container.getAllowedAchievementsUseCase

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
            assertEquals(5L, achievements.getValue("workouts").progress)
            assertEquals(3L, achievements.getValue("sets").progress)
            assertEquals(133L, achievements.getValue("reps").progress)
            assertEquals(2.5, achievements.getValue("reps").achievementGoal)
            assertEquals(0L, achievements.getValue("zero").progress)
            assertEquals(2, achievements.getValue("combined").conditions.size)
            assertEquals(true, achievements.getValue("combined").conditionsSatisfied)
            for (id in listOf("unsupported", "combined")) {
                assertNull(achievements.getValue(id).progress)
                assertNull(achievements.getValue(id).achievementGoal)
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

            driver.execute(null, "ALTER TABLE Achievement DROP COLUMN xp_reward", 0)
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

            driver.execute(null, "ALTER TABLE Achievement DROP COLUMN xp_reward", 0)
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
            val bench = "908d354a-8282-5726-9115-9f322536e428"
            val press = "05997e70-373b-5097-8be8-2037ea2b82f0"
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

            val container = DefaultAppContainer(db, Dispatchers.Unconfined)
            container.loginAsDemoUser()
            assertEquals(
                mapOf("chest" to 3L, "triceps" to 4L, "back" to 1L, "general" to 5L,
                    "sets" to 1L, "reps" to 8L, "wrong-workout" to 0L),
                container.getAllowedAchievementsUseCase().associate { it.id to it.progress }
            )
            val otherUseCase = GetAllowedAchievementsUseCase(
                GamificationRepositoryImpl(db, Dispatchers.Unconfined, {other})
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
            db.addSession("legacy", user, workout, time, "908d354a-8282-5726-9115-9f322536e428", 8)
            removeVersionThreeSchema(driver)

            driver.execute(null, "ALTER TABLE Achievement DROP COLUMN xp_reward", 0)
            RepItDoItDatabase.Schema.migrate(driver, 2, RepItDoItDatabase.Schema.version)
            db.workoutTypeAssignmentQueries.assignWorkoutType(workoutId = workout, workoutType = "CHEST", userId = user)
            db.addAchievement("chest", "EXERCISE_REPS", workoutType = "CHEST")

            val container = DefaultAppContainer(db, Dispatchers.Unconfined)
            container.loginAsDemoUser()
            val useCase = container.getAllowedAchievementsUseCase
            assertEquals(mapOf("general" to 8L, "chest" to 0L), useCase().associate { it.id to it.progress })
            assertEquals("general", db.userAchievementQueries.selectAchievementsByUser(user).executeAsOne().achievement_id)
        }
    }

    @Test
    fun readsCatalogOwnershipAwardsAndUserIsolation() = runBlocking {
        withDatabase { db, _ ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val time = Clock.System.now().toEpochMilliseconds() - 60_000
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.rewardQueries.insertReward("prize", "PRIZE", "Prize", "COLLECTIBLE", "asset", "Description")
            db.rewardQueries.insertReward("badge", "BADGE", "Badge", "BADGE", null, null)
            db.addAchievement("first", "WORKOUT_COUNT")
            db.achievementRewardQueries.insertAchievementReward("first", "prize")
            val repository = GamificationRepositoryImpl(db, Dispatchers.Unconfined) { user }
            val locked = repository.getAchievements().single()
            assertEquals(com.pad.repitdoit.features.gamification.domain.model.AchievementStatus.LOCKED, locked.status)
            assertNull(locked.conditions.single().progress)
            assertEquals("Prize", locked.rewards.single().name)
            assertEquals(setOf("prize", "badge"), repository.getRewards().map { it.id }.toSet())
            assertEquals(false, repository.getRewards().first { it.id == "prize" }.isOwned)

            db.userRewardQueries.insertUserReward("grant", user, "key", time)
            assertEquals(com.pad.repitdoit.features.gamification.domain.model.AchievementStatus.AVAILABLE,
                repository.getAchievements().single().status)
            db.userAchievementQueries.insertUserAchievement("award", user, "first", time)
            db.userRewardQueries.insertUserReward("prize-grant", user, "prize", time)
            val achieved = repository.getAchievements().single()
            assertEquals(time, achieved.availableFrom)
            assertEquals(time, achieved.unlockedAt)
            assertEquals(com.pad.repitdoit.features.gamification.domain.model.AchievementStatus.ACHIEVED, achieved.status)
            assertEquals(time, achieved.rewards.single().obtainedAt)
            assertEquals(false, achieved.conditionsSatisfied)
            val unknown = achieved.conditions.single().copy(progress = null)
            val satisfied = achieved.conditions.single().copy(progress = 10)
            val combined = achieved.copy(conditions = listOf(unknown, satisfied))
            assertNull(combined.conditionsSatisfied)
            assertEquals(true, combined.copy(
                conditionLogic = com.pad.repitdoit.features.gamification.domain.model.ConditionLogic.ANY
            ).conditionsSatisfied)
            assertNull(achieved.copy(conditions = emptyList()).conditionsSatisfied)

            db.userQueries.insertUser("other", "Other", "Other", null)
            val other = GamificationRepositoryImpl(db, Dispatchers.Unconfined) { "other" }
            assertNull(other.getAchievements().single().unlockedAt)
            assertNull(other.getAchievements().single().availableFrom)
            assertEquals(false, other.getRewards().first { it.id == "prize" }.isOwned)
        }
    }

    @Test
    fun readsRoutineAndLevelProgressFromTheBeginningWithoutAwardingAnything() = runBlocking {
        withDatabase { db, driver ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val now = Clock.System.now().toEpochMilliseconds()
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.userRewardQueries.insertUserReward("late-key", user, "key", now)
            db.addAchievement("created", "WORKOUT_CREATED_COUNT", days = 1)
            db.addAchievement("size", "WORKOUT_EXERCISE_COUNT")
            db.addAchievement("level", "USER_LEVEL")
            db.addAchievement("empty", "WORKOUT_EXERCISE_COUNT", workout = "test-workout-002")
            val repository = GamificationRepositoryImpl(db, Dispatchers.Unconfined) { user }
            val initial = repository.getAchievements().associate { it.id to it.progress }
            assertEquals(mapOf("created" to 2L, "size" to 2L, "level" to 6L, "empty" to 0L), initial)

            // Add exercises in successive edits; the count must be per routine.
            for (position in 2L..4L) {
                db.workoutExerciseQueries.insertWorkoutExercise(
                    "extra-$position", "test-workout-001", "908d354a-8282-5726-9115-9f322536e428",
                    position, null, null, null, null, null
                )
            }
            db.workoutQueries.archiveWorkout(now, now, "test-workout-001", user)
            db.userQueries.insertUser("other", "Other", "Other", null)
            db.workoutQueries.insertWorkout("other-routine", "other", "Other", null, null, null, 0, 0)
            driver.execute(null, "UPDATE User SET total_xp = 700 WHERE id = '$user'", 0)
            val updated = repository.getAchievements().associate { it.id to it.progress }
            assertEquals(mapOf("created" to 2L, "size" to 5L, "level" to 5L, "empty" to 0L), updated)
            assertEquals(700L, repository.getTotalXp())
            assertEquals(emptyList(), db.userAchievementQueries.selectAchievementsByUser(user).executeAsList())
            assertEquals(false, repository.getRewards().any { it.isOwned })
        }
    }
    @Test
    fun countsDistinctCatalogExercisesInCompletedSessionsWithoutRequiringSets() = runBlocking {
        withDatabase { db, driver ->
            testDb(db).seed()
            val user = LOCAL_TEST_USER_ID
            val now = Clock.System.now().toEpochMilliseconds()
            val old = now - 3 * 86_400_000L
            val bench = "908d354a-8282-5726-9115-9f322536e428"
            val press = "05997e70-373b-5097-8be8-2037ea2b82f0"
            val third = "994fb7ee-33d7-556a-bf1f-c6f72906a062"
            db.rewardQueries.insertReward("key", "KEY", "Key", "KEY", null, null)
            db.userRewardQueries.insertUserReward("late-key", user, "key", now)
            db.addAchievement("distinct", "DISTINCT_EXERCISE_COUNT", days = 1)
            db.addAchievement("push-only", "DISTINCT_EXERCISE_COUNT", workout = "test-workout-001")
            db.addAchievement("chest-only", "DISTINCT_EXERCISE_COUNT", workoutType = "CHEST")
            db.addAchievement("bench-only", "DISTINCT_EXERCISE_COUNT", exercise = bench)
            db.workoutTypeAssignmentQueries.assignWorkoutType(workoutId = "test-workout-001", workoutType = "CHEST", userId = user)
            db.addSession("old-bench", user, "test-workout-001", old, bench, 0, setCompleted = false)
            // Even an exercise without any sets counts, and repeated appearances count once.
            driver.execute(null, "DELETE FROM SessionSet WHERE id = 'old-bench'", 0)
            db.sessionExerciseQueries.insertSessionExercise(
                id = "duplicate-bench", session_id = "old-bench", exercise_id = bench,
                exercise_name_snapshot = bench, exercise_description_snapshot = null,
                position = 1, notes = null, user_id = user
            )
            db.addSession("repeat-bench", user, "test-workout-002", old, bench, 0)
            db.addSession("old-press", user, "test-workout-002", old, press, 0, setCompleted = false)
            db.addSession("active-third", user, "test-workout-001", old, third, 0, status = "ACTIVE")
            db.addSession("aborted-third", user, "test-workout-001", old, third, 0, status = "ABORTED")
            db.addSession("future-third", user, "test-workout-001", now + 86_400_000L, third, 0)
            db.userQueries.insertUser("other", "Other", "Other", null)
            db.workoutQueries.insertWorkout("other-routine", "other", "Other", null, null, null, old, old)
            db.addSession("other-third", "other", "other-routine", old, third, 0)
            val repository = GamificationRepositoryImpl(db, Dispatchers.Unconfined) { user }
            assertEquals(
                mapOf("distinct" to 2L, "push-only" to 1L, "chest-only" to 1L, "bench-only" to 1L),
                repository.getAchievements().associate { it.id to it.progress }
            )
            assertEquals(1050L, repository.getTotalXp())
            assertEquals(emptyList(), db.userAchievementQueries.selectAchievementsByUser(user).executeAsList())
        }
    }
    private fun removeVersionThreeSchema(driver: JdbcSqliteDriver) {
        driver.execute(null, "ALTER TABLE User DROP COLUMN total_xp", 0)
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
            testDb(RepItDoItDatabase(driver)).loadInitialDatabase()
            // These repository tests define their own gamification catalog.
            driver.execute(null, "DELETE FROM Achievement", 0)
            driver.execute(null, "DELETE FROM Reward", 0)
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
