package com.pad.repitdoit.features.profile.domain.model

data class WorkoutHistoryEntry(
    val sessionId: String,
    val workoutName: String,
    val finishedAt: Long
)