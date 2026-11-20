package com.pad.repitdoit.features.dummy.domain.model

// Representa los datos de un ejercicio dentro de su módulo.
data class Exercise(
    val id: String,
    val name: String,
    val desc: String,
    val imgURL: String,
    val vidURL: String
)