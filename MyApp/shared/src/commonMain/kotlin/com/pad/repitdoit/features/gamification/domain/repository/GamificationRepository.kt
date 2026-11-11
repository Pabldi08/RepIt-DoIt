package com.pad.repitdoit.features.gamification.domain.repository

import com.pad.repitdoit.features.gamification.domain.model.Achievement

interface GamificationRepository {
    suspend fun getAllowedAchievements(): List<Achievement>
}