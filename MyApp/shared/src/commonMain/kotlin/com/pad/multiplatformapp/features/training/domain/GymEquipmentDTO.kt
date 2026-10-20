package com.pad.multiplatformapp.features.training.domain

data class GymEquipmentDTO(
    val id: Long,
    val name: String,
    val description: String? = null,
    val minWeight: Int,
    val maxWeight: Int
)