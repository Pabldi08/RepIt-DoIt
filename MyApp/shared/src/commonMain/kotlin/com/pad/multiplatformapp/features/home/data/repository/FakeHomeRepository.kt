package com.pad.multiplatformapp.features.home.data.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyRoutine
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class FakeHomeRepository: HomeRepository {
    override suspend fun getDailyRoutine(): HomeDailyRoutine {
        return HomeDailyRoutine(
            1,
            "Tren Superior",
            10,
            60
        )

    }
}