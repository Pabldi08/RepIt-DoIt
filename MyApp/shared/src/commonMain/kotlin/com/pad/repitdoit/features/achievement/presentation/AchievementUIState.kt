package com.pad.repitdoit.features.achievement.presentation

import com.pad.repitdoit.features.achievement.domain.model.Achievement

data class AchievementUIState(
    val achievements: List<Achievement> = emptyList(),
    val errorMessage: String? = null
)
