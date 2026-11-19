package com.pad.repitdoit.features.home.domain.usecase

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class GetHomeDailyWorkoutsUseCase(
    private val repository: HomeRepository,
    private val currentUserId: () -> String?
) {

    suspend operator fun invoke(
        weekday: DayOfWeek = Clock.System.todayIn(TimeZone.currentSystemDefault()).dayOfWeek
    ): List<HomeDailyWorkout> {

        val userId = currentUserId()
            ?: return emptyList()

        return repository.getDailyWorkouts(
            userId,
            weekday
        )
    }
}
