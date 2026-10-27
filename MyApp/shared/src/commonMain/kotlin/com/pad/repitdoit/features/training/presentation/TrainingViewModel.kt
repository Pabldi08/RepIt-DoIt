package com.pad.repitdoit.features.training.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.workout.domain.usecase.CreateWorkoutUseCase
import com.pad.repitdoit.features.workout.domain.usecase.GetUsersWorkoutsUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    private val getUsersWorkoutsUseCase: GetUsersWorkoutsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrainingUIState())
    val uiState: StateFlow<TrainingUIState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            val workouts = getUsersWorkoutsUseCase()
            _uiState.update {
                it.copy (
                    workouts = workouts
                )
            }
        }
    }

    fun createWorkout(name: String) {
        viewModelScope.launch {
            try {
                val workout = createWorkoutUseCase(name)
                _uiState.update {
                    it.copy(
                        workouts = it.workouts + workout,
                        errorMessage = null
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
