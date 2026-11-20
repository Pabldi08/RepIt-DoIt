package com.pad.repitdoit.features.gamification.domain.model

// Representa el nivel y el avance que corresponden a la experiencia acumulada.
// Level 1 costs 100 XP to complete; each subsequent level costs 50 more.
data class LevelProgress(
    val level: Int,
    val currentXp: Long,
    val nextLevelXp: Long
)

fun calculateLevelProgress(totalXp: Long): LevelProgress {
    require(totalXp >= 0) { "Total XP cannot be negative" }
    // Completing n levels costs 25 * n * (n + 3). Divide before comparing
    // to avoid overflow, even when totalXp is Long.MAX_VALUE.
    var low = 0L
    var high = Int.MAX_VALUE.toLong() - 1
    while (low < high) {
        val middle = low + (high - low + 1) / 2
        if (middle * (middle + 3) <= totalXp / 25) low = middle
        else high = middle - 1
    }
    val spentXp = 25 * low * (low + 3)
    return LevelProgress(
        level = (low + 1).toInt(),
        currentXp = totalXp - spentXp,
        nextLevelXp = 100 + low * 50
    )
}
