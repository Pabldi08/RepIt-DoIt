package com.pad.repitdoit.features.training.presentation

sealed interface TrainingDialog {
    data object CreateWorkout : TrainingDialog
    data class RenameWorkout(val workoutId: String) : TrainingDialog
    data class AddExercise(val workoutId: String) : TrainingDialog
}
