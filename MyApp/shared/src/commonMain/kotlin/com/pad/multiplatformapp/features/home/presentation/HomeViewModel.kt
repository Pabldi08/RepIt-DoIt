package com.pad.multiplatformapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.multiplatformapp.features.home.domain.usecase.GetHomeDailyWorkoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel (
    private val getHomeDailyWorkoutUseCase: GetHomeDailyWorkoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadDailyRoutine()
    }

    private fun loadDailyRoutine() {
        viewModelScope.launch {
            val routine = getHomeDailyWorkoutUseCase()
            _uiState.update {
                it.copy(
                    dailyRoutine = routine
                )
            }
        }
    }
}
