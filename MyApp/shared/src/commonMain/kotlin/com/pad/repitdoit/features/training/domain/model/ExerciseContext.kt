package com.pad.repitdoit.features.training.domain.model

// Reúne la información de un ejercicio que se muestra en entrenamiento.
data class ExerciseContext(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?
)
