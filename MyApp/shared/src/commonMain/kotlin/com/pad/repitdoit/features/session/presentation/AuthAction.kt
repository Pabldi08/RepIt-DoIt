package com.pad.repitdoit.features.session.presentation

// Define las acciones que pueden emitir los formularios de autenticación.
sealed interface AuthAction {

    data class UsernameChanged(
        val username: String
    ) : AuthAction

    data class DisplayNameChanged(
        val displayName: String
    ) : AuthAction

    data class PasswordChanged(
        val password: String
    ) : AuthAction

    data class RepeatPasswordChanged(
        val repeatPassword: String
    ) : AuthAction

    data object ShowLogin : AuthAction

    data object ShowRegister : AuthAction

    data object Login : AuthAction

    data object Register : AuthAction
}