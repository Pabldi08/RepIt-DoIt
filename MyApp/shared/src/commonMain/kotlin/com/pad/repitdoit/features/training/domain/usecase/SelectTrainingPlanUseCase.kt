package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Establece el plan de entrenamiento activo.
class SelectTrainingPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        repository.selectTrainingPlan(planId)
    }
}
