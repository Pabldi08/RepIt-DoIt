package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.features.training.data.repository.WorkoutRepositoryImpl
import com.pad.repitdoit.features.training.data.repository.FakeWorkoutRepositoryImpl
import com.pad.repitdoit.features.training.domain.model.MuscleGroup
import com.pad.repitdoit.features.training.domain.model.WorkoutCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

// Comprueba cómo se guarda y recupera la categoría de un entrenamiento.
class WorkoutCategoryRepositoryTest {
    @Test
    fun realRepositoryLoadsGroupsWhenCreatingReadingAndAdding() = runBlocking {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            testDb(db).apply { loadInitialDatabase(); seed() }
            val repo = WorkoutRepositoryImpl(db, Dispatchers.Unconfined, { LOCAL_TEST_USER_ID })
            val workout = repo.createWorkout("Mi rutina")
            assertEquals(WorkoutCategory.DEFAULT, workout.category)
            val ex = repo.addWorkoutExercise(workout.id,
                "908d354a-8282-5726-9115-9f322536e428", 10, 3, 20.0, 60)
            assertTrue(MuscleGroup.CHEST in ex.muscleGroups)
            assertTrue(ex.muscleGroups.isNotEmpty())
            val updated = requireNotNull(repo.getWorkout(workout.id))
            assertEquals(ex.muscleGroups, updated.workoutExercises.single().muscleGroups)
            assertEquals(updated, repo.getAllWorkouts().single { it.id == workout.id })
            repo.removeWorkoutExercise(workout.id, ex.id)
            assertEquals(WorkoutCategory.DEFAULT, repo.getWorkout(workout.id)?.category)
        }
    }

    @Test
    fun fakeRepositoryUsesSameClassifier() = runBlocking {
        val repo = FakeWorkoutRepositoryImpl()
        val workout = repo.createWorkout("Mi rutina")
        assertEquals(WorkoutCategory.DEFAULT, workout.category)
        val ex = repo.addWorkoutExercise(workout.id, "40", 10, 3, 20.0, 60)
        assertEquals(setOf(MuscleGroup.BICEPS, MuscleGroup.FOREARMS), ex.muscleGroups)
        assertEquals(WorkoutCategory.BICEPS, repo.getWorkout(workout.id)?.category)
        repo.removeWorkoutExercise(workout.id, ex.id)
        assertEquals(WorkoutCategory.DEFAULT, repo.getWorkout(workout.id)?.category)
    }

    @Test
    fun fakeRepositoryKeepsWorkoutsIsolatedAndPreservesMusclesOnEdit() = runBlocking {
        var userId = "user-a"
        val repo = FakeWorkoutRepositoryImpl { userId }
        val workout = repo.createWorkout("Rutina de A")
        val exercise = repo.addWorkoutExercise(workout.id, "40", 10, 3, 20.0, 60)
        repo.updateWorkoutExercise(workout.id, exercise.copy(weight = 30.0))
        assertEquals(exercise.muscleGroups,
            repo.getWorkout(workout.id)?.workoutExercises?.single()?.muscleGroups)
        assertEquals(WorkoutCategory.BICEPS, repo.getWorkout(workout.id)?.category)

        userId = "user-b"
        assertNull(repo.getWorkout(workout.id))
        assertTrue(repo.getAllWorkouts().none { it.id == workout.id })

        userId = "user-a"
        assertEquals(WorkoutCategory.BICEPS, repo.getWorkout(workout.id)?.category)
        assertEquals(30.0, repo.getWorkout(workout.id)?.workoutExercises?.single()?.weight)
    }
}
