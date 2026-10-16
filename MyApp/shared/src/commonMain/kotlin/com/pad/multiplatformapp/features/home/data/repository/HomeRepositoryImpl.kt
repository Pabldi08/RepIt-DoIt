package com.pad.multiplatformapp.features.home.data.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyWorkout
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl () : HomeRepository {
    override suspend fun getDailyRoutine(): HomeDailyWorkout {
        TODO("Not yet implemented")
    }
}