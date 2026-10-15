package com.pad.multiplatformapp.features.home.domain.repository

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyWorkout

interface HomeRepository {
    suspend fun getDailyRoutine(): HomeDailyWorkout?
}