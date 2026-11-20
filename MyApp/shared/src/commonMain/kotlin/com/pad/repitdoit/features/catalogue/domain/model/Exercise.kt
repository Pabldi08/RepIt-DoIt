package com.pad.repitdoit.features.catalogue.domain.model

// Representa los datos de un ejercicio dentro de su módulo.
data class Exercise(
    val id: String,
    val name: String,
    val description: String?
)
