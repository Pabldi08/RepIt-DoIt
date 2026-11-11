package com.pad.repitdoit.features.training.presentation

sealed interface TrainingAction {
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

    data class renameWorkout(
        val workoutId: String,
        val name: String
    ) : TrainingAction
}