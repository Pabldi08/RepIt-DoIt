package com.pad.multiplatformapp.features.home.domain.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyRoutine

interface HomeRepository {
    suspend fun getDailyRoutine(): HomeDailyRoutine
}