package com.pad.repitdoit.features.training.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.TrainingUseCasesAC
import com.pad.repitdoit.features.training.presentation.action.WorkoutExerciseSetupAction
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseSetupState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutExerciseSetupViewModel (
    private val appContainer: TrainingUseCasesAC,
    private val exerciseId: String,
    private val workoutId: String? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutExerciseSetupState())
    val uiState: StateFlow<WorkoutExerciseSetupState> = _uiState.asStateFlow()

    init {
        loadExercise()
        loadWorkouts()
    }

    fun onAction(action: WorkoutExerciseSetupAction) {
        when(action) {
            is WorkoutExerciseSetupAction.SelectWorkout -> selectWorkout(action.workoutId)
            is WorkoutExerciseSetupAction.ChangeWeight -> changeWeight(action.value)
            is WorkoutExerciseSetupAction.ChangeNumReps -> changeNumReps(action.value)
            is WorkoutExerciseSetupAction.ChangeNumSeries -> changeNumSeries(action.value)
            is WorkoutExerciseSetupAction.ChangeRestSec -> changeRestSec(action.value)
            WorkoutExerciseSetupAction.SendForm -> sendForm()
        }
    }

    private fun loadExercise() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoadingExercise = true, exerciseError = null)
            }
            try {
                val exercise = appContainer.getExerciseContextUseCase(exerciseId)
                _uiState.update {
                    it.copy(
                        exerciseContext = exercise,
                        isLoadingExercise = false,
                        exerciseError = if (exercise == null) {
                            "El ejercicio no existe o no está disponible"
                        } else null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingExercise = false,
                        exerciseError = "No se pudo cargar la información del ejercicio"
                    )
                }
            }
        }
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy (
                    isLoadingWorkouts = true,
                    workoutError = null
                )
            }
            try {
                if (workoutId != null) {
                    val workout = requireNotNull(
                        appContainer.getWorkoutUseCase(workoutId)
                    ) {
                        "La rutina no está disponible"
                    }

                    _uiState.update {
                        it.copy(
                            selectedWorkout = workout,
                            isLoadingWorkouts = false
                        )
                    }
                } else {
                    val workouts = appContainer.getUsersWorkoutsUseCase()

                    _uiState.update {
                        it.copy(
                            availableWorkouts = workouts,
                            isLoadingWorkouts = false
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy (
                        isLoadingWorkouts = false,
                        workoutError = "No se pudieron cargar las rutinas"
                    )
                }
            }
        }
    }

    private fun selectWorkout(workoutId: String) {
        if (_uiState.value.isSaving || _uiState.value.isSaved) return
        _uiState.update { state ->
            val workout = state.availableWorkouts.find { it.id == workoutId }
            if (workout == null) state else state.copy(
                selectedWorkout = workout,
                workoutError = null,
                saveError = null
            )
        }
    }

    private fun changeWeight(value: String) {
        _uiState.update {
            if (it.isSaving || it.isSaved) it
            else {
                it.copy(
                    weight = value,
                    weightError = null,
                    saveError = null
                )
            }
        }
    }

    private fun changeNumReps(value: String) {
        _uiState.update {
            if (it.isSaving || it.isSaved) it
            else {
                it.copy(
                    numReps = value,
                    numRepsError = null,
                    saveError = null
                )
            }
        }
    }

    private fun changeNumSeries(value: String) {
        _uiState.update {
            if (it.isSaving || it.isSaved) it
            else {
                it.copy(
                    numSeries = value,
                    numSeriesError = null,
                    saveError = null
                )
            }
        }
    }

    private fun changeRestSec(value: String) {
        _uiState.update {
            if (it.isSaving || it.isSaved) it
            else {
                it.copy(
                    restSec = value,
                    restSecError = null,
                    saveError = null
                )
            }
        }
    }

    private fun sendForm() {
        val state = _uiState.value
        if (state.isSaving || state.isSaved) return

        val weight = state.weight.trim().replace(',', '.').toDoubleOrNull()
        val numReps = state.numReps.trim().toIntOrNull()
        val numSeries = state.numSeries.trim().toIntOrNull()
        val restSec = state.restSec.trim().toIntOrNull()
        val workout = state.selectedWorkout

        val weightError = if (weight == null || !weight.isFinite() || weight < 0.0) {
            "Introduce un peso válido mayor o igual que cero"
        } else null
        val numRepsError = if (numReps == null || numReps <= 0) {
            "Introduce un número entero de repeticiones mayor que cero"
        } else null
        val numSeriesError = if (numSeries == null || numSeries <= 0) {
            "Introduce un número entero de series mayor que cero"
        } else null
        val restSecError = if (restSec == null || restSec < 0) {
            "Introduce un descanso entero mayor o igual que cero"
        } else null

        _uiState.update {
            it.copy(
                weightError = weightError,
                numRepsError = numRepsError,
                numSeriesError = numSeriesError,
                restSecError = restSecError,
                saveError = when {
                    workout == null -> "Selecciona una rutina antes de guardar"
                    exerciseId.isBlank() -> "El identificador del ejercicio no es válido"
                    else -> null
                }
            )
        }

        if (
            weightError != null || numRepsError != null ||
            numSeriesError != null || restSecError != null ||
            workout == null || exerciseId.isBlank()
        ) return

        // Se marca antes de lanzar la corrutina para bloquear dobles pulsaciones.
        _uiState.update {
            it.copy(
                isSaving = true
            )
        }
        viewModelScope.launch {
            try {
                appContainer.addExerciseToWorkoutUseCase(
                    workoutId = workout.id,
                    exerciseId = exerciseId,
                    weight = requireNotNull(weight),
                    numReps = requireNotNull(numReps),
                    numSeries = requireNotNull(numSeries),
                    restSec = requireNotNull(restSec)
                )
                _uiState.update {
                    it.copy(
                        isSaved = true,
                        saveError = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        saveError = "No se pudo añadir el ejercicio a la rutina"
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isSaving = false
                    )
                }
            }
        }
    }
}
