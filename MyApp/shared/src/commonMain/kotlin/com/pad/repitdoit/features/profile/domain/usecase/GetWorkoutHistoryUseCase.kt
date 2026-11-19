package com.pad.repitdoit.features.profile.domain.usecase

import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry
import com.pad.repitdoit.features.profile.domain.repository.WorkoutHistoryRepository

class GetWorkoutHistoryUseCase(
    private val repository: WorkoutHistoryRepository,
    private val currentUserId: () -> String?
) {
    suspend operator fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<WorkoutHistoryEntry> {
        require(fromTimestamp < toTimestamp) { "El intervalo de fechas no es válido" }
        val userId = checkNotNull(currentUserId()) { "No hay ninguna sesión activa" }

        return repository.getCompletedSessions(userId, fromTimestamp, toTimestamp)
    }
}