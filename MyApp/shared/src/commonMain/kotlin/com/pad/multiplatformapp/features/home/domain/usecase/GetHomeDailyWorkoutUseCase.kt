package com.pad.multiplatformapp.features.home.domain.usecase

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyWorkout
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class GetHomeDailyWorkoutUseCase (
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): HomeDailyWorkout? {
        return repository.getDailyRoutine()
    }
}