package com.pad.repitdoit.features.profile.domain.repository

import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry

interface WorkoutHistoryRepository {
    suspend fun getCompletedSessions(
        userId: String,
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<WorkoutHistoryEntry>
}