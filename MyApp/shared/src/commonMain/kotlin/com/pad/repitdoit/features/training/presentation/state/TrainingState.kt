package com.pad.repitdoit.features.training.presentation.state

import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.model.Workout

// Mantiene los planes, entrenamientos y selecciones que se muestran en la interfaz.
data class TrainingState(
    val editingExerciseId: String? = null,
    val exerciseEdit: WorkoutExerciseEditState = WorkoutExerciseEditState(),
    val isLoadingInitialData: Boolean = true,
    val isRefreshing: Boolean = false,
    val workouts: List<Workout> = emptyList(),
    val plans: List<TrainingPlan> = emptyList(),
    val selectedPlanId: String? = null,
    val isLoadingPlans: Boolean = false,
    val isSavingPlan: Boolean = false,
    val plansError: String? = null,
    val errorMessage: String? = null
) {
    val selectedPlan: TrainingPlan?
        get() = plans.find {
            it.id == selectedPlanId
        }
}

data class WorkoutExerciseEditState(
    val weight: String? = null,
    val numSeries: String? = null,
    val numReps: String? = null,
    val restSec: String? = null,
    val restSecError: String? = null,
    val weightError: String? = null,
    val numSeriesError: String? = null,
    val numRepsError: String? = null,
    val saveError: String? = null
)
