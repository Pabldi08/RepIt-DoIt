package com.pad.repitdoit.features.session.domain.model

// Representa la sesión del usuario autenticado.
data class Session(
    val id: String,
    val userId: String
)