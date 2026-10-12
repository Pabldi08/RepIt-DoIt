package com.pad.multiplatformapp.navigation

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
    data class Routine (
        val routineId: Int
    ) : Route
}