package com.pad.repitdoit.features.training.presentation.action

sealed interface WorkoutExerciseSetupAction {
    data class SelectWorkout(
        val workoutId: String
    ) : WorkoutExerciseSetupAction

    data class ChangeWeight(
        val value: String
    ) : WorkoutExerciseSetupAction

    data class ChangeNumReps(
        val value: String
    ) : WorkoutExerciseSetupAction

    data class ChangeNumSeries(
        val value: String
    ) : WorkoutExerciseSetupAction

    data class ChangeRestSec(
        val value: String
    ) : WorkoutExerciseSetupAction

    data object SendForm : WorkoutExerciseSetupAction
}