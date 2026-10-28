package com.pad.repitdoit.features.workout.domain.repository

import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.model.WorkoutExercise

interface WorkoutRepository {
    suspend fun getWorkout(workoutId: String): Workout?

    suspend fun createWorkout(name: String): Workout
    suspend fun renameWorkout(
        workoutId: String,
        name: String
    )
    suspend fun removeWorkout(workoutId: String)

    suspend fun addWorkoutExercise(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int
    ) : WorkoutExercise
    suspend fun updateWorkoutExercise(
        workoutId: String,
        workoutExercise: WorkoutExercise
    )
    suspend fun removeWorkoutExercise(
        workoutId: String,
        workoutExerciseId: String
    )

    suspend fun getAllWorkouts(): List<Workout>

}