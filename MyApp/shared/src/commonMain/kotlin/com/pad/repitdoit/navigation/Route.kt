package com.pad.repitdoit.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Home: Route

    @Serializable
    data object Profile: Route

    @Serializable
    data object ProfileSettings: Route

    @Serializable
    data object Gamification: Route

    @Serializable
    data object QrCamera: Route

    @Serializable
    data object Training: Route

    @Serializable
    data class Workout (
        val workoutId: String
    ) : Route

    @Serializable
    data class Catalogue(
        val machineId: String? = null,
        val workoutId: String? = null
    ) : Route

    @Serializable
    data class WorkoutExerciseSetup(
        val exerciseId: String,
        val workoutId: String? = null,
        val returnToTraining: Boolean = false,
        val returnWorkoutId: String? = null
    ) : Route
}
