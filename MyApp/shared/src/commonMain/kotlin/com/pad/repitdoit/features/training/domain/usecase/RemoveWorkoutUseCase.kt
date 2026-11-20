package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Elimina un entrenamiento del usuario.
class RemoveWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String) {
        require(workoutId.isNotBlank()) { "El identificador del entrenamiento no puede estar vacío" }
        repository.removeWorkout(workoutId)
    }
}
