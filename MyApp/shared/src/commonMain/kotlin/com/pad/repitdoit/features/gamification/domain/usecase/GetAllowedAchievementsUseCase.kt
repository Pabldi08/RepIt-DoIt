package com.pad.repitdoit.features.gamification.domain.usecase

import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

// Consulta los logros que el usuario puede obtener.
// Accessible achievements with at least one condition; includes previously awarded ones.
class GetAllowedAchievementsUseCase (
    private val repository: GamificationRepository
){
    suspend operator fun invoke(): List<Achievement>{
        return repository.getAllowedAchievements()
    }
}