package com.pad.multiplatformapp.features.workout.presentation

import com.pad.multiplatformapp.features.workout.domain.model.Workout

data class WorkoutUIState (
    val workout: Workout? = null
)