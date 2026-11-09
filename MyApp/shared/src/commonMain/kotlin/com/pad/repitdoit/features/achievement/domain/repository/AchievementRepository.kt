package com.pad.repitdoit.features.achievement.domain.repository

import com.pad.repitdoit.features.achievement.domain.model.Achievement

interface AchievementRepository {
    suspend fun getAllowedAchievements(): List<Achievement>
}