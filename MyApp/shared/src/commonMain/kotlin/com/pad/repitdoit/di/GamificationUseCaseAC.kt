package com.pad.repitdoit.di

import com.pad.repitdoit.features.gamification.domain.usecase.AwardCompletedAchievementsUseCase

import com.pad.repitdoit.features.gamification.domain.usecase.GetAllowedAchievementsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetAchievementsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetRewardsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase

interface GamificationUseCaseAC {
    val awardCompletedAchievementsUseCase: AwardCompletedAchievementsUseCase
    val getAchievementsUseCase: GetAchievementsUseCase
    val getTotalXpUseCase: GetTotalXpUseCase
    val getRewardsUseCase: GetRewardsUseCase
    val getAllowedAchievementsUseCase: GetAllowedAchievementsUseCase
}
