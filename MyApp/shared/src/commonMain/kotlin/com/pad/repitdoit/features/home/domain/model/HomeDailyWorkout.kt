package com.pad.repitdoit.features.home.domain.model

// Representa un entrenamiento que el inicio muestra como previsto para el día.
data class HomeDailyWorkout(
    val id: String,
    val name: String,
    val exerciseCount: Int
)
