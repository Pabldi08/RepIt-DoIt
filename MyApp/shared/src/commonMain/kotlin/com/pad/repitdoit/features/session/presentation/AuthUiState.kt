package com.pad.repitdoit.features.session.presentation

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val username: String = "",
    val displayName: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    override fun toString(): String =
        "AuthUiState(mode=$mode, isLoading=$isLoading)"
}