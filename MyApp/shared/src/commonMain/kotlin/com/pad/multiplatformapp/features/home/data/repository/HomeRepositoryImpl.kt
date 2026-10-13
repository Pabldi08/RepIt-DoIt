package com.pad.multiplatformapp.features.home.data.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyRoutine
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl () : HomeRepository {
    override suspend fun getDailyRoutine(): HomeDailyRoutine {
        TODO("Not yet implemented")
    }
}