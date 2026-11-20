package com.pad.repitdoit.features.session.domain.model

// Agrupa las credenciales que utilizan los flujos de autenticación.
data class UserCredentials(
    val username: String,
    val displayName: String?,
    val password: String
) {
    override fun toString(): String =
        "UserCredentials(username=$username, displayName=$displayName)"
}