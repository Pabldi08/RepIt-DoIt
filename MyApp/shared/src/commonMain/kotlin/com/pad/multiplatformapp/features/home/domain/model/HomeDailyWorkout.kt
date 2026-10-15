package com.pad.multiplatformapp.features.home.domain.model

data class HomeDailyWorkout(
    val id: Long,
    val name: String,
    val exerciseCount: Int,
    val estimatedTime: Int
)