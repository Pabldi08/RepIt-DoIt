package com.pad.repitdoit.di

import com.pad.repitdoit.features.achievement.domain.usecase.GetAllowedAchievementsUseCase

interface AchievementUseCaseAC {
    val getAllowedAchievementsUseCase: GetAllowedAchievementsUseCase
}