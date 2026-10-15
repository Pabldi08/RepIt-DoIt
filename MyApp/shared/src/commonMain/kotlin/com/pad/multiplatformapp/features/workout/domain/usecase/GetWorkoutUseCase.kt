package com.pad.multiplatformapp.features.workout.domain.usecase

import com.pad.multiplatformapp.features.workout.domain.model.Workout
import com.pad.multiplatformapp.features.workout.domain.repository.WorkoutRepository

class GetWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: Long): Workout? {
        return repository.getWorkout(workoutId)
    }
}