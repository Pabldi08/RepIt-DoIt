package com.pad.repitdoit.features.gamification.domain.model

data class Achievement(
    val id: String,
    val name: String,
    val desc: String,
    // Both are null when the conditions cannot be represented as one progress counter.
    val progress: Long?,
    val achivementGoal: Double?
)
