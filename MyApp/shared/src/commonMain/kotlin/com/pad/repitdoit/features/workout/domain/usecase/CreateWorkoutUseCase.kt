package com.pad.repitdoit.features.workout.domain.usecase

import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository

class CreateWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke (
        name: String
    ) : Workout {
      return repository.createWorkout(name)
    }
}