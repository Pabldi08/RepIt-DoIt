package com.pad.repitdoit.features.training.domain.model

// Representa una sesión asignada a un día del plan semanal.
data class ScheduledWorkout(
    val id: String,
    val workoutId: String,
    val workoutName: String,
    val numEx: Int,
    val weekday: Int,
    val position: Int
)
