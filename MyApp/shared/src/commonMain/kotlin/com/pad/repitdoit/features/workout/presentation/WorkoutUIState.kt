package com.pad.repitdoit.features.workout.presentation

import com.pad.repitdoit.features.training.domain.model.Workout

data class WorkoutUIState (
    val workout: Workout? = null,
    val errorMessage: String? = null
)