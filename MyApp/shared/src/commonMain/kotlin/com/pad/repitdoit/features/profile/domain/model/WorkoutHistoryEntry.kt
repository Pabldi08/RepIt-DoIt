package com.pad.repitdoit.features.profile.domain.model

// Representa una sesión completada que aparece en el historial.
data class WorkoutHistoryEntry(
    val sessionId: String,
    val workoutName: String,
    val startedAt : Long,
    val finishedAt: Long
){
    val durationMinutes: Long get() = (finishedAt - startedAt) / 60_000L
}