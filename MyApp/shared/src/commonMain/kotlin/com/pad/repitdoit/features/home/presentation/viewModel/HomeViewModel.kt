package com.pad.repitdoit.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.HomeUseCasesAC
import com.pad.repitdoit.features.gamification.domain.model.calculateLevelProgress
import com.pad.repitdoit.features.home.presentation.state.HomeUIState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Carga los entrenamientos del día y el progreso que se muestran en inicio.
class HomeViewModel (
    private val appContainer: HomeUseCasesAC
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()
    private var progressLoadJob: Job? = null
    private var profileLoadJob: Job? = null

    fun loadUsername() {
        profileLoadJob?.cancel()
        profileLoadJob = viewModelScope.launch {
            val username = try {
                appContainer.getProfileUseCase().username
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                null
            }
            _uiState.update { it.copy(username = username) }
        }
    }

    // Convierte la experiencia acumulada en el progreso que muestra la pantalla.
    fun loadProgress() {
        progressLoadJob?.cancel()
        progressLoadJob = viewModelScope.launch {
            _uiState.update { it.copy(progressLoadFailed = false) }
            try {
                val progress = calculateLevelProgress(appContainer.getTotalXpUseCase())
                _uiState.update { it.copy(levelProgress = progress) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(progressLoadFailed = true) }
            }
        }
    }

    // Actualiza la lista que se muestra como programación del día.
    fun loadDailyWorkouts() {
        viewModelScope.launch {
            val workouts = appContainer.getHomeDailyWorkoutsUseCase()
            _uiState.update {
                it.copy(
                    dailyWorkouts = workouts
                )
            }
        }
    }
}