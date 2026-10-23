package com.pad.repitdoit.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Home: Route

    @Serializable
    data object Profile: Route

    @Serializable
    data object Scanner: Route

    @Serializable
    data object Training: Route

    @Serializable
    data class Workout (
        val workoutId: String
    ) : Route
}