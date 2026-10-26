package com.pad.repitdoit.features.workout.domain.model

data class SessionExercise(
    val id: String,
    val sessionId: String,
    val exerciseId: String,
    val position: Int,
    val notes: String? = null
)
