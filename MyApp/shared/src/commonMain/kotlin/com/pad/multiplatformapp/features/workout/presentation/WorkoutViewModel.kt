package com.pad.multiplatformapp.features.workout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.multiplatformapp.features.workout.domain.usecase.GetWorkoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel (
    private val getWorkoutUseCase: GetWorkoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutUIState())
    val uiState: StateFlow<WorkoutUIState> = _uiState.asStateFlow()

    fun loadWorkout(workoutId: Long) {
        viewModelScope.launch {
            val workout = getWorkoutUseCase(workoutId)
            _uiState.update {
                it.copy(
                    workout = workout
                )
            }
        }
    }
}