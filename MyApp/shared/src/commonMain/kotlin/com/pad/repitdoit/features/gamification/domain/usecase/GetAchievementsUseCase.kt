package com.pad.repitdoit.features.gamification.domain.usecase

import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

// Consulta los logros del usuario.
class GetAchievementsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): List<Achievement> = repository.getAchievements()
}
