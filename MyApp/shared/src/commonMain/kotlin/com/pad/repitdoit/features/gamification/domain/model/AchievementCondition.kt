package com.pad.repitdoit.features.gamification.domain.model

data class AchievementCondition(
    val id: String,
    val type: String,
    val comparisonOperator: String,
    val targetValue: Double,
    val exerciseId: String?,
    val workoutId: String?,
    val workoutType: String?,
    val periodDays: Long?,
    // Null means unavailable or unsupported, never zero.
    val progress: Long?
) {
    val isSatisfied: Boolean? get() {
        if (comparisonOperator != ">=" || !targetValue.isFinite() || targetValue < 0) return null
        return progress?.let { it.toDouble() >= targetValue }
    }
}
