package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Consulta los planes de entrenamiento del usuario.
class GetUsersTrainingPlansUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(): List<TrainingPlan> = repository.getTrainingPlans()
}
