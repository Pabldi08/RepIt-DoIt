package com.pad.repitdoit.features.home.domain.usecase

import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout
import com.pad.repitdoit.features.home.domain.repository.HomeRepository

class GetHomeDailyWorkoutsUseCase (
    private val repository: HomeRepository
) {
    suspend operator fun invoke(userId: String, weekday: Int): List<HomeDailyWorkout> {
        println("USECASE: Repositorio utilizado = ${repository::class.simpleName}")
        println("USECASE: userId=$userId, weekday=$weekday")

        val workouts = repository.getDailyWorkouts(userId, weekday)

        println("HOME-USECASE: Resultado = ${workouts.size}")

        return workouts
    }
}