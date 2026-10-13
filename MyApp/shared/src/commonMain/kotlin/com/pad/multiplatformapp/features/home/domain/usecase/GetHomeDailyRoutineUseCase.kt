package com.pad.multiplatformapp.features.home.domain.usecase

import com.pad.multiplatformapp.features.home.domain.model.HomeDailyRoutine
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository

class GetHomeDailyRoutineUseCase (
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): HomeDailyRoutine? {
        return repository.getDailyRoutine()
    }
}