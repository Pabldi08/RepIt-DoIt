package com.pad.multiplatformapp.features.home.presentation

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyWorkout

data class HomeUIState (
    val dailyRoutine: HomeDailyWorkout? = null
)