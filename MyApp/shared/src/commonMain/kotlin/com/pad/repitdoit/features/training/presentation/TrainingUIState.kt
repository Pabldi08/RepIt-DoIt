package com.pad.repitdoit.features.training.presentation

import com.pad.repitdoit.features.training.domain.model.Workout

data class TrainingUIState(
    val workouts: List<Workout> = emptyList(),
    val errorMessage: String? = null
)
