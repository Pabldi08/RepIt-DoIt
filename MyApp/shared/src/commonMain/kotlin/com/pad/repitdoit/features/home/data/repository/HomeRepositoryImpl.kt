package com.pad.repitdoit.features.home.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : HomeRepository {

    override suspend fun getDailyWorkouts(
        userId: String,
        weekday: Int
    ): List<HomeDailyWorkout> = withContext(dispatcher) {

        require(weekday in 1..7) {
            "Dia de la semana debe estar entre 1 y 7"
        }

        db.workoutScheduleQueries
            .selectDailyWorkouts(
                user_id = userId,
                weekday = weekday.toLong()
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