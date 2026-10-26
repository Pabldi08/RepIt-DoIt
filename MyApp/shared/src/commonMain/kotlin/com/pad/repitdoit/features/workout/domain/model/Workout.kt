package com.pad.repitdoit.features.workout.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class Workout(
    val id: String,
    val userId: String,
    val name: String,
    val description: String? = null,
    val estimatedTime: Int? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val archivedAt: Long? = null
)