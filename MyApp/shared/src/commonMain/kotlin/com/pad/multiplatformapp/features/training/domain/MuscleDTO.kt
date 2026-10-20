package com.pad.multiplatformapp.features.training.domain

data class MuscleDTO(
    val id: Long,
    val name: String,
    val group: MuscleGroup,
    val stretchsIdList: List<Int>
)