package com.pad.repitdoit.features.training.domain.model

// Agrupa las sesiones de un plan de entrenamiento semanal.
data class TrainingPlan(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val schedule: List<ScheduledWorkout> = emptyList()
)
