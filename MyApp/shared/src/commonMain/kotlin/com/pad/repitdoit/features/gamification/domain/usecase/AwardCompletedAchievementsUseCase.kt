package com.pad.repitdoit.features.gamification.domain.usecase

import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

// Concede los logros cuyas condiciones ya se han cumplido.
class AwardCompletedAchievementsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke() = repository.awardCompletedAchievements()
}
