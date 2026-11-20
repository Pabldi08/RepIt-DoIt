package com.pad.repitdoit.features.gamification.domain.usecase

import com.pad.repitdoit.features.gamification.domain.model.Reward
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

// Consulta las recompensas del usuario.
class GetRewardsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): List<Reward> = repository.getRewards()
}
