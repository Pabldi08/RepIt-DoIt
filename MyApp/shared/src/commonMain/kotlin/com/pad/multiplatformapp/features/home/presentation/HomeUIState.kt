package com.pad.multiplatformapp.features.home.presentation

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyRoutine

data class HomeUIState (
    val dailyRoutine: HomeDailyRoutine? = null
)