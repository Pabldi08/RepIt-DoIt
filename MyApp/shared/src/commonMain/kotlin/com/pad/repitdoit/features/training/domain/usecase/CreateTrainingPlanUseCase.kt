package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class CreateTrainingPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(name: String): TrainingPlan {
        val trimmedName = name.trim()
        require(trimmedName.isNotEmpty()) { "El nombre del plan no puede estar vacío" }
        return repository.createTrainingPlan(trimmedName)
    }
}
