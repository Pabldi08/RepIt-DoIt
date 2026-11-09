package com.pad.repitdoit.features.achievement.domain.usecase

import com.pad.repitdoit.features.achievement.domain.model.Achievement
import com.pad.repitdoit.features.achievement.domain.repository.AchievementRepository

class GetAllowedAchievementsUseCase (
    private val repository: AchievementRepository
){
    suspend operator fun invoke(): List<Achievement>{
        return repository.getAllowedAchievements()
    }
}