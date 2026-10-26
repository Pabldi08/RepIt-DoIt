package com.pad.repitdoit.features.workout.domain.model

data class Exercise(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val isActive: Boolean,
    val updatedAt: Long,
    val equipmentIds: List<String>,
    val musclesIds: List<String>
)