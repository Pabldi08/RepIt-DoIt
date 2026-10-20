package com.pad.multiplatformapp.features.training.domain

data class ExerciseDTO(
    val id: Long,
    val name: String,
    val description: String,
    val difficulty: Difficulty,
    val machineId: Long?,
    val primaryMuscleId: Long,
    val secondaryMuscleIds: List<Long> = emptyList()
)
