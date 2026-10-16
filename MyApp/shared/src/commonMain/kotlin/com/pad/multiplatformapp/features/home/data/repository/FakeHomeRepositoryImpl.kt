package com.pad.multiplatformapp.features.home.data.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyWorkout
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class FakeHomeRepositoryImpl: HomeRepository {
    override suspend fun getDailyRoutine(): HomeDailyWorkout {
        return HomeDailyWorkout(
            1,
            "Tren Superior",
            10,
            60
        )

    }
}