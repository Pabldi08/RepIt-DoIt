package com.pad.repitdoit.features.achievement.data.repository

import com.pad.repitdoit.features.achievement.domain.model.Achievement
import com.pad.repitdoit.features.achievement.domain.repository.AchievementRepository

class FakeAchievementsRepositoryImpl: AchievementRepository {
    override suspend fun getAllowedAchievements(): List<Achievement> = emptyList()
}