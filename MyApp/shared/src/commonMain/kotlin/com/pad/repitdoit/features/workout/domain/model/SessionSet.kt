package com.pad.repitdoit.features.workout.domain.model

data class SessionSet(
    val id: String,
    val sessionExerciseId: String,
    val setNumber: Int,
    val weightKg: Double? = null,
    val reps: Int? = null,
    val durationSeconds: Int? = null,
    val completedAt: Long? = null
)
