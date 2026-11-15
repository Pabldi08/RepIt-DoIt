package com.pad.repitdoit.features.training.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.TrainingUseCasesAC
import com.pad.repitdoit.features.training.presentation.action.TrainingAction
import com.pad.repitdoit.features.training.presentation.state.TrainingState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseEditState

class TrainingViewModel(
    private val appContainer: TrainingUseCasesAC
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrainingState())
    val uiState: StateFlow<TrainingState> = _uiState.asStateFlow()

    private val exerciseSaveMutex = Mutex()

    // El Root solicita la carga al entrar y al regresar de otros destinos.
    fun refresh() {
        if (_uiState.value.isRefreshing) return
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            try {
                coroutineScope {
                    launch { loadWorkouts() }
                    launch { loadPlans() }
                }
            } finally {
                _uiState.update {
                    it.copy(isLoadingInitialData = false, isRefreshing = false)
                }
            }
        }
    }

    fun onAction(action: TrainingAction) {
        when(action) {
            is TrainingAction.SelectExercise -> selectExercise(action.workoutExerciseId)
            is TrainingAction.ChangeRestSec -> editExercise(action.workoutExerciseId, ExerciseField.Rest, action.value)
            is TrainingAction.ChangeWeight -> editExercise(action.workoutExerciseId, ExerciseField.Weight, action.value)
            is TrainingAction.ChangeNumSeries -> editExercise(action.workoutExerciseId, ExerciseField.Series, action.value)
            is TrainingAction.ChangeNumReps -> editExercise(action.workoutExerciseId, ExerciseField.Reps, action.value)
            is TrainingAction.RemoveWorkout -> removeWorkout(action.workoutId)
            is TrainingAction.CreatePlan -> updatePlan("No se pudo crear el plan") {
                val plan = appContainer.createTrainingPlanUseCase(action.name)
                appContainer.selectTrainingPlanUseCase(plan.id)
            }
            is TrainingAction.RenamePlan -> updatePlan("No se pudo renombrar el plan") {
                appContainer.renameTrainingPlanUseCase(action.planId, action.name)
            }
            is TrainingAction.DeletePlan -> updatePlan("No se pudo eliminar el plan") {
                appContainer.deleteTrainingPlanUseCase(action.planId)
            }
            is TrainingAction.AddWorkoutToPlan -> updatePlan("No se pudo añadir la rutina al plan") {
                appContainer.addWorkoutToPlanUseCase(action.planId, action.workoutId, action.weekday)
            }
            is TrainingAction.RemoveWorkoutFromPlan -> updatePlan("No se pudo quitar la rutina del plan") {
                appContainer.removeWorkoutFromPlanUseCase(action.planId, action.scheduleId)
            }
            is TrainingAction.MoveWorkoutToPlanDay -> updatePlan("No se pudo cambiar la rutina de día") {
                appContainer.moveWorkoutToPlanDayUseCase(action.planId, action.scheduleId, action.weekday)
            }
            is TrainingAction.createWorkout -> createWorkout(action.name)
            is TrainingAction.SelectPlan -> selectPlan(action.planId)
            is TrainingAction.renameWorkout -> renameWorkout(action.workoutId, action.name)
            is TrainingAction.addExerciseToWorkout -> addExerciseToWorkout(
                action.workoutId,
                action.exerciseId,
                action.numReps,
                action.numSeries,
                action.weight,
                action.restSec
            )
            is TrainingAction.removeExerciseFromWorkout -> removeExerciseFromWorkout(
                workoutId = action.workoutId,
                workoutExerciseId = action.workoutExerciseId
            )
        }
    }
    private fun selectExercise(id: String) {
        _uiState.update { state ->
            if (state.editingExerciseId == id) state
            else state.copy(editingExerciseId = id, exerciseEdit = WorkoutExerciseEditState())
        }
    }

    private enum class ExerciseField { Weight, Series, Reps, Rest }

    private fun editExercise(id: String, field: ExerciseField, value: String) {
        selectExercise(id)
        val weight = value.trim().replace(',', '.').toDoubleOrNull()
        val count = value.trim().toIntOrNull()
        val error = when (field) {
            ExerciseField.Rest -> if (count == null || count < 0)
                "Introduce un descanso entero mayor o igual que cero" else null
            ExerciseField.Weight -> if (weight == null || !weight.isFinite() || weight < 0.0)
                "Introduce un peso válido mayor o igual que cero" else null
            ExerciseField.Series -> if (count == null || count <= 0)
                "Introduce un número entero de series mayor que cero" else null
            ExerciseField.Reps -> if (count == null || count <= 0)
                "Introduce un número entero de repeticiones mayor que cero" else null
        }
        _uiState.update { state ->
            val edit = state.exerciseEdit
            val updated = when (field) {
                ExerciseField.Rest -> edit.copy(restSec = value, restSecError = error)
                ExerciseField.Weight -> edit.copy(weight = value, weightError = error)
                ExerciseField.Series -> edit.copy(numSeries = value, numSeriesError = error)
                ExerciseField.Reps -> edit.copy(numReps = value, numRepsError = error)
            }
            state.copy(exerciseEdit = updated)
        }
        if (error != null) return
        viewModelScope.launch {
            // Leer dentro del bloqueo conserva los cambios previos de otros campos.
            exerciseSaveMutex.withLock {
                try {
                    val workout = _uiState.value.workouts.find { workout ->
                        workout.workoutExercises.any { it.id == id }
                    } ?: return@withLock
                    val exercise = workout.workoutExercises.first { it.id == id }
                    val updated = when (field) {
                        ExerciseField.Rest -> exercise.copy(restSec = requireNotNull(count))
                        ExerciseField.Weight -> exercise.copy(weight = requireNotNull(weight))
                        ExerciseField.Series -> exercise.copy(numSets = requireNotNull(count))
                        ExerciseField.Reps -> exercise.copy(numReps = requireNotNull(count))
                    }
                    appContainer.updateWorkoutExerciseUseCase(workout.id, updated)
                    _uiState.update { state ->
                        val edit = state.exerciseEdit
                        state.copy(
                            workouts = state.workouts.map { current ->
                                if (current.id == workout.id) current.copy(
                                    workoutExercises = current.workoutExercises.map {
                                        if (it.id == id) updated else it
                                    }
                                ) else current
                            },
                            exerciseEdit = if (state.editingExerciseId == id) edit.copy(saveError = null) else edit
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _uiState.update { state ->
                        val edit = state.exerciseEdit
                        if (state.editingExerciseId != id) state
                        else state.copy(exerciseEdit = edit.copy(saveError = "No se pudo guardar el cambio. Vuelve a introducir el valor para reintentarlo."))
                    }
                }
            }
        }
    }

    private fun selectPlan(planId: String) {
        updatePlan("No se pudo seleccionar el plan") {
            appContainer.selectTrainingPlanUseCase(planId)
        }
    }

    private fun updatePlan(errorMessage: String, operation: suspend () -> Unit) {
        if (_uiState.value.isSavingPlan) return
        _uiState.update { it.copy(isSavingPlan = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                operation()
                val plans = appContainer.getUsersTrainingPlansUseCase()
                _uiState.update { state ->
                    state.copy(
                        plans = plans,
                        selectedPlanId = plans.firstOrNull { it.isActive }?.id,
                        plansError = null,
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = if (e is IllegalArgumentException) e.message ?: errorMessage else errorMessage)
                }
            } finally {
                _uiState.update { it.copy(isSavingPlan = false) }
            }
        }
    }

    private suspend fun loadPlans() {
        _uiState.update { it.copy(isLoadingPlans = true, plansError = null) }
        try {
            val plans = appContainer.getUsersTrainingPlansUseCase()
            _uiState.update { state ->
                state.copy(
                    plans = plans,
                    selectedPlanId = plans.firstOrNull { it.isActive }?.id,
                    isLoadingPlans = false,
                    plansError = null
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoadingPlans = false, plansError = "No se pudieron cargar los planes")
            }
        }
    }

    private suspend fun loadWorkouts() {
            try {
                val workouts = appContainer.getUsersWorkoutsUseCase()
                _uiState.update { it.copy(workouts = workouts, errorMessage = null) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "No se pudieron cargar los entrenamientos") }
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

                _uiState.update { state ->
                    val workouts = state.workouts.map { workout ->
                        if (workout.id == workoutId) {
                            workout.copy(
                                workoutExercises = workout.workoutExercises + addedExercise
                            )
                        } else workout
                    }
                    val exerciseCount = workouts.find { it.id == workoutId }?.workoutExercises?.size
                    state.copy(
                        workouts = workouts,
                        plans = state.plans.map { plan ->
                            plan.copy(schedule = plan.schedule.map { entry ->
                                if (entry.workoutId == workoutId && exerciseCount != null) {
                                    entry.copy(numEx = exerciseCount)
                                } else entry
                            })
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

    private fun removeWorkout(workoutId: String) {
        viewModelScope.launch {
            try {
                appContainer.removeWorkoutUseCase(workoutId)
                _uiState.update { state ->
                    state.copy(
                        workouts = state.workouts.filterNot { it.id == workoutId },
                        plans = state.plans.map { plan ->
                            plan.copy(schedule = plan.schedule.filterNot { it.workoutId == workoutId })
                        },
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "No se pudo eliminar la rutina")
                }
            }
        }
    }

    private fun removeExerciseFromWorkout(
        workoutId: String,
        workoutExerciseId: String
    ) {
        viewModelScope.launch {
            try {
                appContainer.removeExerciseFromWorkoutUseCase(
                    workoutId = workoutId,
                    workoutExerciseId = workoutExerciseId
                )
                _uiState.update { state ->
                    val workouts = state.workouts.map { workout ->
                        if (workout.id == workoutId) {
                            workout.copy(
                                workoutExercises = workout.workoutExercises.filterNot {
                                    it.id == workoutExerciseId
                                }
                            )
                        } else workout
                    }
                    val exerciseCount = workouts.find { it.id == workoutId }?.workoutExercises?.size
                    state.copy(
                        workouts = workouts,
                        plans = state.plans.map { plan ->
                            plan.copy(schedule = plan.schedule.map { entry ->
                                if (entry.workoutId == workoutId && exerciseCount != null) {
                                    entry.copy(numEx = exerciseCount)
                                } else entry
                            })
                        },
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "No se pudo quitar el ejercicio del entrenamiento")
                }
            }
        }
    }
}
