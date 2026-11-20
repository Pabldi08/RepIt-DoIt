package com.pad.repitdoit.features.training.presentation.action


// Define los eventos que recibe el flujo de planes y entrenamientos.
//Actions to execute on Training Screens
sealed interface TrainingAction {
    data class SelectExercise(val workoutExerciseId: String) : TrainingAction
    data class RemoveWorkout(val workoutId: String) : TrainingAction
    data class CreatePlan(
        val name: String
    ) : TrainingAction

    data class SelectPlan(
        val planId: String
    ) : TrainingAction

    data class RenamePlan(
        val planId: String,
        val name: String
    ) : TrainingAction

    data class DeletePlan(
        val planId: String
    ) : TrainingAction

    // Weekday: 1 = Monday, 7 = Sunday
    data class AddWorkoutToPlan(
        val planId: String,
        val workoutId: String,
        val weekday: Int
    ) : TrainingAction

    data class RemoveWorkoutFromPlan(
        val planId: String,
        val scheduleId: String
    ) : TrainingAction

    data class MoveWorkoutToPlanDay(
        val planId: String,
        val scheduleId: String,
        val weekday: Int
    ) : TrainingAction

    data class createWorkout(
        val name: String
    ) : TrainingAction

    data class addExerciseToWorkout(
        val workoutId: String,
        val exerciseId: String,
        val numReps: Int,
        val numSeries: Int,
        val weight: Double,
        val restSec: Int
    ) : TrainingAction

    data class removeExerciseFromWorkout(
        val workoutId: String,
        val workoutExerciseId: String
    ) : TrainingAction

    data class ChangeWeight(
        val workoutExerciseId: String,
        val value: String
    ) : TrainingAction

    data class ChangeNumSeries(
        val workoutExerciseId: String,
        val value: String
    ) : TrainingAction

    data class ChangeRestSec(
        val workoutExerciseId: String,
        val value: String
    ) : TrainingAction

    data class ChangeNumReps(
        val workoutExerciseId: String,
        val value: String
    ) : TrainingAction

    data class renameWorkout(
        val workoutId: String,
        val name: String
    ) : TrainingAction
}
