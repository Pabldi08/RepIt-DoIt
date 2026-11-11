package com.pad.repitdoit.features.training.presentation

sealed interface TrainingDialog {
    data object CreateWorkout : TrainingDialog
    data class RenameWorkout(val workoutId: String) : TrainingDialog
}
