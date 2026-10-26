package com.pad.repitdoit.features.workout.domain.model

data class Muscle(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val muscleGroup: String
)
