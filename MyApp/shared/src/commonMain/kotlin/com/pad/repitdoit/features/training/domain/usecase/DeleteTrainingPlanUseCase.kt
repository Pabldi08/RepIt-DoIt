package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Elimina un plan de entrenamiento.
class DeleteTrainingPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        repository.deleteTrainingPlan(planId)
    }
}
