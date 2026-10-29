package com.pad.repitdoit.features.workout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.training.domain.usecase.CreateWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetWorkoutUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel (
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutUIState())
    val uiState: StateFlow<WorkoutUIState> = _uiState.asStateFlow()

    fun loadWorkout(workoutId: String) {
        viewModelScope.launch {
            val workout = getWorkoutUseCase(workoutId)
            _uiState.update {
                it.copy(
                    workout = workout
                )
            }
        }
    }

    fun createWorkout(name: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        errorMessage = null
                    )
                }
                val workout = createWorkoutUseCase(name)
                _uiState.update {
                    it.copy(
                        workout = workout
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (w: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo crear el entrenamiento"
                    )
                }
            }
        }
    }
}