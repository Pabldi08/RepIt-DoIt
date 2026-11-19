package com.pad.repitdoit.features.home.presentation.state

import com.pad.repitdoit.features.gamification.domain.model.LevelProgress
import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout

data class HomeUIState (
    val dailyWorkouts: List<HomeDailyWorkout> = emptyList(),
    val levelProgress: LevelProgress? = null,
    val progressLoadFailed: Boolean = false,
    val username: String? = null
)