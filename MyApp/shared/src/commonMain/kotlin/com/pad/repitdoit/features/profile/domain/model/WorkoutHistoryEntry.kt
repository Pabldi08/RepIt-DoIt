package com.pad.repitdoit.features.profile.domain.model

data class WorkoutHistoryEntry(
    val sessionId: String,
    val workoutName: String,
    val startedAt : Long,
    val finishedAt: Long
){
    val durationMinutes: Long get() = (finishedAt - startedAt) / 60_000L
}