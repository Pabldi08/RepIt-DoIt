package com.pad.repitdoit.features.gamification.data.repository

import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

class FakeGamificationRepositoryImpl: GamificationRepository {
    override suspend fun getAllowedAchievements(): List<Achievement> = emptyList()
}