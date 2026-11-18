package com.pad.repitdoit.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetWorkoutHistoryUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    getProfileUseCase: GetProfileUseCase,
    private val getWorkoutHistoryUseCase: GetWorkoutHistoryUseCase,
    private val getTotalXpUseCase: GetTotalXpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = getProfileUseCase()
            _uiState.update { it.copy(profile = profile) }
        }
    }

    fun loadWorkoutHistory(fromTimestamp: Long, toTimestamp: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isHistoryLoading = true, historyError = null) }

            try {
                val history = getWorkoutHistoryUseCase(fromTimestamp, toTimestamp)
                _uiState.update {
                    it.copy(workoutHistory = history, isHistoryLoading = false)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isHistoryLoading = false,
                        historyError = "No se pudo cargar el historial."
                    )
                }
            }
        }
    }

    fun loadTotalXp() {
        viewModelScope.launch {
            val totalXp = try {
                getTotalXpUseCase()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                null
            }
            _uiState.update { it.copy(totalXp = totalXp) }
        }
    }
}

