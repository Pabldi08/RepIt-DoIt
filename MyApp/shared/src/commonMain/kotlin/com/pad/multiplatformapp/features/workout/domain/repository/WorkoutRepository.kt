package com.pad.multiplatformapp.features.workout.domain.repository

import com.pad.multiplatformapp.features.workout.domain.model.Workout
import com.pad.multiplatformapp.features.workout.domain.model.WorkoutExercise

interface WorkoutRepository {
    suspend fun getWorkout(workoutId: Long): Workout?

    suspend fun createWorkout(name: String): Workout
    suspend fun renameWorkout(
        workoutId: Long,
        name: String
    )
    suspend fun removeWorkout(workoutId: Long)

    suspend fun addWorkoutExercise(
        workoutId: Long,
        exerciseId: Long,
        numReps: Int,
        numSeries: Int
    ) : WorkoutExercise
    suspend fun updateWorkoutExercise(
        workoutId: Long,
        workoutExercise: WorkoutExercise
    )
    suspend fun removeWorkoutExercise(
        workoutId: Long,
        workoutExerciseId: Long
    )


}