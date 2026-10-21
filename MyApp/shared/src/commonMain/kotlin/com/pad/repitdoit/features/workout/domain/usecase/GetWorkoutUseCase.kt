package com.pad.repitdoit.features.workout.domain.usecase

import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository

class GetWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String): Workout? {
        return repository.getWorkout(workoutId)
    }
}