package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class GetUsersWorkoutsUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(): List<Workout> {
        return repository.getAllWorkouts()
    }
}