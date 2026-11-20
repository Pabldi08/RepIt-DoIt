package com.pad.repitdoit.features.gamification.domain.usecase

import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository

// Consulta la experiencia total acumulada.
class GetTotalXpUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): Long = repository.getTotalXp()
}
