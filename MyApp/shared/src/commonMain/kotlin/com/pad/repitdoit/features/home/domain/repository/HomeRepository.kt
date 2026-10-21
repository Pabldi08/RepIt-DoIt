package com.pad.repitdoit.features.home.domain.repository

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout

interface HomeRepository {
    suspend fun getDailyWorkouts(
        userId: String,
        weekday: Int
    ): List<HomeDailyWorkout>
}