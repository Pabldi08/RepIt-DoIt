package com.pad.repitdoit.di

import com.pad.repitdoit.features.gamification.domain.usecase.GetAllowedAchievementsUseCase

interface GamificationUseCaseAC {
    val getAllowedAchievementsUseCase: GetAllowedAchievementsUseCase
}