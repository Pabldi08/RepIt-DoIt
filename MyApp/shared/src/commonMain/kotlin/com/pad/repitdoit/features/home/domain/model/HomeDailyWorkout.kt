package com.pad.repitdoit.features.home.domain.model

data class HomeDailyWorkout(
    val id: String,
    val name: String,
    val exerciseCount: Int,
    val estimatedTime: Int?
)