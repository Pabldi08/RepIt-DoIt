
package com.pad.repitdoit.features.session.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.usecase.CreateUserUseCase
import com.pad.repitdoit.features.session.domain.usecase.LoginUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Gestiona los formularios de acceso y registro y sus resultados.
class AuthViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AuthAction) {

        // Ignora nuevas acciones mientras hay una solicitud de autenticación en curso.
        if (_uiState.value.isLoading) return

        when (action) {

            is AuthAction.UsernameChanged ->
                _uiState.update {
                    it.copy(
                        username = action.username,
                        error = null
                    )
                }

            is AuthAction.DisplayNameChanged ->
                _uiState.update {
                    it.copy(
                        displayName = action.displayName,
                        error = null
                    )
                }

            is AuthAction.PasswordChanged ->
                _uiState.update {
                    it.copy(
                        password = action.password,
                        error = null
                    )
                }

            is AuthAction.RepeatPasswordChanged ->
                _uiState.update {
                    it.copy(
                        repeatPassword = action.repeatPassword,
                        error = null
                    )
                }

            AuthAction.ShowLogin ->
                changeMode(AuthMode.LOGIN)

            AuthAction.ShowRegister ->
                changeMode(AuthMode.REGISTER)

            AuthAction.Login ->
                login()

            AuthAction.Register ->
                register()
        }
    }

    private fun changeMode(mode: AuthMode) {
        _uiState.update {
            AuthUiState(
                mode = mode,
                username = it.username
            )
        }
    }

    private fun login() {

        val state = _uiState.value

        if (state.username.isBlank() ||
            state.password.isBlank()
        ) {
            showError("Completa todos los campos")
            return
        }

        val credentials = UserCredentials(
            username = state.username.trim(),
            displayName = null,
            password = state.password
        )

        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {

            try {
                val session = loginUseCase(credentials)

                if (session == null) {
                    showError(
                        "Usuario o contraseña incorrectos"
                    )
                } else {
                    _uiState.value = AuthUiState()
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                showError(
                    "No se ha podido iniciar sesión"
                )
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    // Valida los campos del registro antes de solicitar la creación de la cuenta.
    private fun register() {

        val state = _uiState.value

        val error = when {
            state.username.isBlank() ->
                "Introduce un nombre de usuario"

            state.displayName.isBlank() ->
                "Introduce un nombre visible"

            state.password.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres"

            state.password != state.repeatPassword ->
                "Las contraseñas no coinciden"

            else -> null
        }

        if (error != null) {
            showError(error)
            return
        }

        val credentials = UserCredentials(
            username = state.username.trim(),
            displayName = state.displayName.trim(),
            password = state.password
        )

        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {

            var accountCreated = false

            try {
                createUserUseCase(credentials)

                accountCreated = true

                val session = loginUseCase(credentials)

                if (session != null) {
                    _uiState.value = AuthUiState()
                } else {
                    registrationCompleted()
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                if (accountCreated) {
                    registrationCompleted()
                } else {
                    showError(
                        "No se ha podido crear la cuenta. " +
                                "Comprueba que el usuario no exista."
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun registrationCompleted() {
        _uiState.update {
            AuthUiState(
                mode = AuthMode.LOGIN,
                username = it.username,
                error = "Cuenta creada. Inicia sesión."
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(error = message)
        }
    }
}
