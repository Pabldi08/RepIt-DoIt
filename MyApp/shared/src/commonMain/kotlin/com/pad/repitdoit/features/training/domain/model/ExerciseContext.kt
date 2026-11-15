package com.pad.repitdoit.features.training.domain.model

data class ExerciseContext(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?
)
