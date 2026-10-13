package com.pad.multiplatformapp.features.home.domain.model

data class HomeDailyRoutine(
    val id: Long,
    val name: String,
    val exerciseCount: Int,
    val estimatedMinutes: Int
)