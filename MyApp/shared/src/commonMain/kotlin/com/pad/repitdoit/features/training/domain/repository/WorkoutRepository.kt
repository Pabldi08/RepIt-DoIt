package com.pad.repitdoit.features.training.domain.repository

import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.model.ExerciseContext
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise

interface WorkoutRepository {
    suspend fun getTrainingPlans(): List<TrainingPlan>
    suspend fun selectTrainingPlan(planId: String)
    suspend fun createTrainingPlan(name: String): TrainingPlan
    suspend fun renameTrainingPlan(planId: String, name: String)
    suspend fun deleteTrainingPlan(planId: String)
    suspend fun addWorkoutToPlan(planId: String, workoutId: String, weekday: Int)
    suspend fun removeWorkoutFromPlan(planId: String, scheduleId: String)
    suspend fun moveWorkoutToPlanDay(planId: String, scheduleId: String, weekday: Int)
    suspend fun getExerciseContext(exerciseId: String): ExerciseContext?
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
        numSeries: Int,
        weight: Double,
        restSec: Int
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
