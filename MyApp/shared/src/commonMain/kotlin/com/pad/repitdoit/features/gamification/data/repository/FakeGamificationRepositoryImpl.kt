package com.pad.repitdoit.features.gamification.data.repository

import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

import com.pad.repitdoit.features.gamification.domain.model.Reward

// Ofrece una implementación de prueba para trabajar con la experiencia, los logros y las recompensas.
class FakeGamificationRepositoryImpl: GamificationRepository {
    override suspend fun awardCompletedAchievements() = Unit
    override suspend fun getTotalXp(): Long = 0
    override suspend fun getAchievements(): List<Achievement> = emptyList()
    override suspend fun getRewards(): List<Reward> = emptyList()
    override suspend fun getAllowedAchievements(): List<Achievement> = emptyList()
}
