package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.ExerciseContext
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Recupera la información de contexto de un ejercicio.
class GetExerciseContextUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(exerciseId: String): ExerciseContext? {
        require(exerciseId.isNotBlank()) { "El identificador del ejercicio no puede estar vacío" }
        return repository.getExerciseContext(exerciseId)
    }
}
