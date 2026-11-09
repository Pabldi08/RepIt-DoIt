package com.pad.repitdoit.features.achievement.domain.model

data class Achievement(
    val id: String,
    val name: String,
    val desc: String,
    val progress: Int,
    val achivementGoal: Int
)
