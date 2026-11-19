package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import kotlin.test.Test
import kotlin.test.assertEquals

class InitialCatalogTest {
    @Test
    fun loadsCatalogWithForeignKeysAndCanBeRepeated() {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            val loader = testDb(db)
            loader.loadInitialDatabase()
            val original = db.exerciseQueries.selectAllExercises().executeAsList()
            val exercise = original.first()
            db.exerciseQueries.updateExercise(
                "Personalizado", exercise.description, exercise.image_url,
                exercise.video_url, exercise.is_active, 42L, exercise.id
            )
            loader.loadInitialDatabase()

            val achievements = db.achievementQueries.selectAllAchievements().executeAsList()
            assertEquals(12, achievements.size)
            assertEquals(listOf(50L, 100L, 150L, 250L, 400L, 750L, 100L, 150L, 50L, 75L, 100L, 150L),
                achievements.sortedBy { it.code }.map { it.xp_reward })
            val predecessors = mapOf(
                "A02" to "A01", "A03" to "A02", "A04" to "A03", "A05" to "A04",
                "A06" to "A05", "D02" to "D01", "F02" to "F01"
            )
            assertEquals(14, db.rewardQueries.selectAllRewards().executeAsList().size)
            assertEquals("PROFILE_FRAME", db.rewardQueries.selectRewardByCode("R01").executeAsOne().reward_type)
            assertEquals(15L, db.rewardQueries.selectRewardByCode("R02").executeAsOne().unlock_level)
            achievements.forEach {
                assertEquals(
                    if (it.code in predecessors) "KEY_${it.code}" else "KEY_LEVEL_0",
                    it.required_reward_id
                )
                assertEquals(1, db.achievementConditionQueries.selectConditionsByAchievement(it.id).executeAsList().size)
            }
            predecessors.forEach { (next, previous) ->
                val key = db.rewardQueries.selectRewardByCode("KEY_$next").executeAsOne()
                assertEquals("KEY", key.reward_type)
                assertEquals(key.id,
                    db.achievementRewardQueries.selectAchievementReward(previous, key.id).executeAsOne().reward_id)
            }
            assertEquals(setOf("R08", "KEY_A04"), db.achievementRewardQueries
                .selectRewardsByAchievement("A03").executeAsList().map { it.reward_id }.toSet())
            assertEquals("DISTINCT_EXERCISE_COUNT", db.achievementConditionQueries.selectConditionsByAchievement("D02").executeAsOne().condition_type)
            val exercises = db.exerciseQueries.selectAllExercises().executeAsList()
            assertEquals(112, exercises.size)
            assertEquals(35, db.equipmentQueries.selectAllEquipment().executeAsList().size)
            assertEquals(19, db.muscleQueries.selectAllMuscles().executeAsList().size)
            assertEquals(261, exercises.sumOf {
                db.muscleExerciseQueries.selectMuscleExercisesByExercise(it.id).executeAsList().size
            })
            assertEquals(194, exercises.sumOf {
                db.exerciseEquipmentQueries.selectEquipmentByExercise(it.id).executeAsList().size
            })
            assertEquals("Peso corporal", db.equipmentQueries
                .selectEquipmentById("82746ac5-02c6-5bf2-856b-f529a6e0d86a").executeAsOne().name)
            assertEquals("Personalizado", db.exerciseQueries.selectExerciseById(exercise.id).executeAsOne().name)
            assertEquals(42L, db.exerciseQueries.selectExerciseById(exercise.id).executeAsOne().updated_at)

            loader.seed()
            loader.seed()
            assertEquals(1, db.userRewardQueries.selectRewardsByUser(LOCAL_TEST_USER_ID).executeAsList().size)
            val second = db.achievementQueries.selectAchievementByCode("A02").executeAsOne()
            db.achievementQueries.updateAchievement(
                "KEY_LEVEL_0", second.code, second.name, second.description,
                second.condition_logic, second.icon_url, second.id
            )
            db.achievementRewardQueries.deleteAchievementReward("A01", "KEY_A02")
            db.userAchievementQueries.insertUserAchievement("old-award", LOCAL_TEST_USER_ID, "A01", 42L)
            loader.loadInitialDatabase()
            loader.loadInitialDatabase()
            val owned = db.userRewardQueries.selectRewardsByUser(LOCAL_TEST_USER_ID).executeAsList()
            assertEquals(setOf("KEY_LEVEL_0", "KEY_A02"), owned.map { it.reward_id }.toSet())
            assertEquals(42L, owned.single { it.reward_id == "KEY_A02" }.obtained_at)
            assertEquals("KEY_A02",
                db.achievementQueries.selectAchievementByCode("A02").executeAsOne().required_reward_id)
            assertEquals(exercises, db.exerciseQueries.selectAllExercises().executeAsList())
            val workoutExercises = db.workoutExerciseQueries
                .selectExercisesByWorkout("test-workout-001").executeAsList()
            assertEquals(
                listOf("908d354a-8282-5726-9115-9f322536e428", "05997e70-373b-5097-8be8-2037ea2b82f0"),
                workoutExercises.map { it.exercise_id }
            )
            assertEquals(listOf(4L, 3L), workoutExercises.map { it.target_sets })
            assertEquals(listOf(10L, 12L), workoutExercises.map { it.target_reps })
        }
    }
}
