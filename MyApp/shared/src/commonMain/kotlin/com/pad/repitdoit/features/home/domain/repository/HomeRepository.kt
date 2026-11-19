package com.pad.repitdoit.features.home.domain.repository

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import kotlinx.datetime.DayOfWeek

interface HomeRepository {
    suspend fun getDailyWorkouts(
        userId: String,
        weekday: DayOfWeek
    ): List<HomeDailyWorkout>
}
