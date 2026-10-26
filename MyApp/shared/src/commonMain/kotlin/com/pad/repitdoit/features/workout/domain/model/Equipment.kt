package com.pad.repitdoit.features.workout.domain.model

data class Equipment(
    val id: String,
    val name: String,
    val description: String? = null,
    val isActive: Boolean,
    val updatedAt: Long
)
