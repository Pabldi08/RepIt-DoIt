package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class CreateWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke (
        name: String
    ) : Workout {
      return repository.createWorkout(name)
    }
}