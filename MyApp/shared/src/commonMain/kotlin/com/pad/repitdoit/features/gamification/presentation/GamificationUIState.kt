package com.pad.repitdoit.features.gamification.presentation

import com.pad.repitdoit.features.gamification.domain.model.Achievement

data class GamificationUIState(
    val achievements: List<Achievement> = emptyList(),
    val errorMessage: String? = null
)
