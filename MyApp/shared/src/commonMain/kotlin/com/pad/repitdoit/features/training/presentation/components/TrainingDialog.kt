package com.pad.repitdoit.features.training.presentation.components

sealed interface TrainingDialog {
    data class RemoveWorkout(
        val workoutId: String
    ) : TrainingDialog
    data class RemoveExerciseFromWorkout(
        val workoutId: String,
        val workoutExerciseId: String
    ) : TrainingDialog

    data object CreatePlan : TrainingDialog
    data class DeletePlan(
        val planId: String
    ) : TrainingDialog
    data class AddWorkoutToPlan(
        val planId: String,
        val weekday: Int
    ) : TrainingDialog
    data object CreateWorkout : TrainingDialog
    data class RenameWorkout(
        val workoutId: String
    ) : TrainingDialog
    data class RenamePlan(
        val planId: String
    ) : TrainingDialog
}
