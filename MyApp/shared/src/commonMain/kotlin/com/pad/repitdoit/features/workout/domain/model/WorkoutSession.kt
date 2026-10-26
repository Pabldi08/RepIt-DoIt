package com.pad.repitdoit.features.workout.domain.model

data class WorkoutSession(
    val id: String,
    val userId: String,
    val workoutId: String,
    val startedAt: Long,
    val finishedAt: Long? = null,
    val status: String,
    val notes: String? = null
)