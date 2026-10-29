package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class GetWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String): Workout? {
        return repository.getWorkout(workoutId)
    }
}