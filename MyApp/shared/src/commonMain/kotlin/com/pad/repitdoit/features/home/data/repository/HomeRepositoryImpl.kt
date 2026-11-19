package com.pad.repitdoit.features.home.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

class HomeRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : HomeRepository {

    override suspend fun getDailyWorkouts(
        userId: String,
        weekday: DayOfWeek
    ): List<HomeDailyWorkout> = withContext(dispatcher) {

        db.workoutScheduleQueries
            .selectDailyWorkouts(
                user_id = userId,
                weekday = weekday.isoDayNumber.toLong()
            )
            .executeAsList()
            .map { workout ->

                HomeDailyWorkout(
                    id = workout.id,
                    name = workout.name,
                    exerciseCount = workout.exercise_count.toInt()
                )
            }
    }
}
