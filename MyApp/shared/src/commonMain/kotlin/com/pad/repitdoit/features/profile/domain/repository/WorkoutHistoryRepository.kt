package com.pad.repitdoit.features.profile.domain.repository

import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry

// Define las operaciones para consultar o gestionar el historial de entrenamientos completados.
interface WorkoutHistoryRepository {
    suspend fun getCompletedSessions(
        userId: String,
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<WorkoutHistoryEntry>
}