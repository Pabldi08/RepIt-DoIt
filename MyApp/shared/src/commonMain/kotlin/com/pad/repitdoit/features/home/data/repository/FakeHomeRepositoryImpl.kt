package com.pad.repitdoit.features.home.data.repository

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import kotlinx.datetime.DayOfWeek

class FakeHomeRepositoryImpl: HomeRepository {
    override suspend fun getDailyWorkouts(
        userId: String,
        weekday: DayOfWeek
    ): List<HomeDailyWorkout> {
        return listOf(
            HomeDailyWorkout(
                "001",
                "Tren Superior",
                10
            )
        )

    }
}
