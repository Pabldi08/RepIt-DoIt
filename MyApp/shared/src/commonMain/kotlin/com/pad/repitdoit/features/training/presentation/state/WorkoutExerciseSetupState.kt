package com.pad.repitdoit.features.training.presentation.state

import com.pad.repitdoit.features.training.domain.model.ExerciseContext
import com.pad.repitdoit.features.training.domain.model.Workout

data class WorkoutExerciseSetupState(
    val exerciseContext: ExerciseContext? = null,
    val availableWorkouts: List<Workout> = emptyList(),
    val selectedWorkout: Workout? = null,
    val weight: String = "",
    val numReps: String = "",
    val numSeries: String = "",
    val restSec: String = "",

    val isLoadingWorkouts: Boolean = true,
    val isLoadingExercise: Boolean = true,

    val workoutError: String? = null,
    val exerciseError: String? = null,
    val weightError: String? = null,
    val numSeriesError: String? = null,
    val numRepsError: String? = null,
    val restSecError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val saveError: String? = null
)
