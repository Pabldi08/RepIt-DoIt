package com.pad.multiplatformapp.features.achievements.presentation


data class AchievementsUIState (
    // se necesitaria tener una clase logro/achievement
    val achievementList: List<Int>? = null,
    val xpLevel: Int,
    val xpTillNextLevel: Int
)