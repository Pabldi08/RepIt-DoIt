package com.pad.repitdoit.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import com.pad.repitdoit.features.session.domain.usecase.LogoutUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = getProfileUseCase()

            _uiState.update {
                it.copy(profile = profile)
            }
        }
    }

    fun logout() {

        if (_uiState.value.isLoggingOut) return

        _uiState.update {
            it.copy(isLoggingOut = true, error = null)
        }

        viewModelScope.launch {

            try {
                logoutUseCase()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        error = "No se ha podido cerrar sesión"
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isLoggingOut = false)
                }
            }
        }
    }
}