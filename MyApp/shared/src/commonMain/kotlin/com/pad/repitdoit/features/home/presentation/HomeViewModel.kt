package com.pad.repitdoit.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.home.domain.usecase.GetHomeDailyWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel (
    private val getHomeDailyWorkoutsUseCase: GetHomeDailyWorkoutsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadDailyWorkouts()
    }

    private fun loadDailyWorkouts() {
        viewModelScope.launch {
            val workouts = getHomeDailyWorkoutsUseCase("test-user-001", 1)
            println("HOME_VM: Rutinas recibidas: ${workouts.size}")
            _uiState.update {
                it.copy(
                    dailyWorkouts = workouts
                )
            }
            println("HOME_VM: Rutinas en el estado: ${_uiState.value.dailyWorkouts.size}")
        }
    }
}
