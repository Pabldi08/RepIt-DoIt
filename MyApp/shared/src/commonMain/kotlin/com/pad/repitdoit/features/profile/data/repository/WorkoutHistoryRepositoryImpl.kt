package com.pad.repitdoit.features.profile.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry
import com.pad.repitdoit.features.profile.domain.repository.WorkoutHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WorkoutHistoryRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : WorkoutHistoryRepository {

    override suspend fun getCompletedSessions(
        userId: String,
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<WorkoutHistoryEntry> = withContext(dispatcher) {
        db.workoutSessionQueries
            .selectCompletedWorkoutSessionsByUser(userId, fromTimestamp, toTimestamp)
            .executeAsList()
            .map { session ->
                WorkoutHistoryEntry(
                    sessionId = session.id,
                    workoutName = session.workout_name_snapshot,
                    finishedAt = checkNotNull(session.finished_at)
                )
            }
    }
}