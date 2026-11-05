package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class RemoveWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String) {
        require(workoutId.isNotBlank()) { "El identificador de la rutina no puede estar vacio" }
        repository.removeWorkout(workoutId)
    }
}
