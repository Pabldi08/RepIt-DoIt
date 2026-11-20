package com.pad.repitdoit.features.gamification.domain.repository

import com.pad.repitdoit.features.gamification.domain.model.Achievement

import com.pad.repitdoit.features.gamification.domain.model.Reward

// Define las operaciones para consultar o gestionar la experiencia, los logros y las recompensas.
interface GamificationRepository {
    suspend fun awardCompletedAchievements()
    suspend fun getTotalXp(): Long
    suspend fun getAchievements(): List<Achievement>
    suspend fun getRewards(): List<Reward>
    suspend fun getAllowedAchievements(): List<Achievement>
}
