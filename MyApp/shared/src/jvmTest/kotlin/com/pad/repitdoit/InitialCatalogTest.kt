package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
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
