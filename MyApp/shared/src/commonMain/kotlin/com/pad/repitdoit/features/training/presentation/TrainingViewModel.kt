package com.pad.repitdoit.features.training.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.TrainingUseCasesAC
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO: Hacer acciones por caso de uso
class TrainingViewModel(
    private val appContainer: TrainingUseCasesAC
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrainingState())
    val uiState: StateFlow<TrainingState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    fun onAction(action: TrainingAction) {
        when(action) {
            is TrainingAction.createWorkout -> createWorkout(action.name)
            is TrainingAction.renameWorkout -> renameWorkout(action.workoutId, action.name)
            is TrainingAction.addExerciseToWorkout -> addExerciseToWorkout(
                action.workoutId,
                action.exerciseId,
                action.numReps,
                action.numSeries,
                action.weight,
                action.restSec
            )
        }
    }
    fun loadWorkouts() {
        viewModelScope.launch {
            val workouts = appContainer.getUsersWorkoutsUseCase()
            _uiState.update {
                it.copy (
                    workouts = workouts
                )
            }
        }
    }

    private fun createWorkout(name: String) {
        viewModelScope.launch {
            try {
                val workout = appContainer.createWorkoutUseCase(name)
                _uiState.update {
                    it.copy(
                        workouts = it.workouts + workout,
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo crear el entrenamiento"
                    )
                }
            }
        }
    }

    private fun renameWorkout(workoutId: String, name: String) {
        viewModelScope.launch {
            try {
                val savedName = appContainer.renameWorkoutUseCase(
                    workoutId = workoutId,
                    name = name
                )
                _uiState.update {
                    it.copy(
                        workouts = it.workouts.map { workout ->
                            if (workout.id == workoutId) {
                                workout.copy(name = savedName)
                            } else {
                                workout
                            }
                        },
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo cambiar el nombre del entrenamiento"
                    )
                }
            }
        }
    }

    private fun addExerciseToWorkout(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int,
        weight: Double,
        restSec: Int
    ) {
        viewModelScope.launch {
            try {
                val addedExercise = appContainer.addExerciseToWorkoutUseCase(
                    workoutId = workoutId,
                    exerciseId = exerciseId,
                    numReps = numReps,
                    numSeries = numSeries,
                    weight = weight,
                    restSec = restSec
                )

                _uiState.update { it ->
                    it.copy(
                        workouts = it.workouts.map { workout ->
                            if (workout.id == workoutId) {
                                workout.copy(
                                    workoutExercises =
                                        workout.workoutExercises + addedExercise
                                )
                            } else {
                                workout
                            }
                        },
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo añadir el ejercicio al entrenamiento"
                    )
                }
            }
        }
    }
}
