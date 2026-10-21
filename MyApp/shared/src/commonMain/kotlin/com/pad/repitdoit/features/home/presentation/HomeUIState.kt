package com.pad.repitdoit.features.home.presentation

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout

data class HomeUIState (
    val dailyWorkouts: List<HomeDailyWorkout> = emptyList()
)