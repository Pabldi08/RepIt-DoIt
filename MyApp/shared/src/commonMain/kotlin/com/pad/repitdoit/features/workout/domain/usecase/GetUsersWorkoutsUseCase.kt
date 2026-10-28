package com.pad.repitdoit.features.workout.domain.usecase

import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository

class GetUsersWorkoutsUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(): List<Workout> {
        return repository.getAllWorkouts()
    }
}