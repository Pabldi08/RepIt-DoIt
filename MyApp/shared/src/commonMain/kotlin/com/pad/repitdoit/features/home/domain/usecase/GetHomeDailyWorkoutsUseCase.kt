package com.pad.repitdoit.features.home.domain.usecase

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository

class GetHomeDailyWorkoutsUseCase(
    private val repository: HomeRepository,
    private val currentUserId: () -> String?
) {

    suspend operator fun invoke(
        weekday: Int
    ): List<HomeDailyWorkout> {

        val userId = currentUserId()
            ?: return emptyList()

        return repository.getDailyWorkouts(
            userId,
            weekday
        )
    }
}