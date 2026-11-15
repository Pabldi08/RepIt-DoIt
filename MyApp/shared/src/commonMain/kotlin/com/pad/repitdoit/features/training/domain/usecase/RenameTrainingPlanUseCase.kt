package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class RenameTrainingPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String, name: String) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        val trimmedName = name.trim()
        require(trimmedName.isNotEmpty()) { "El nombre del plan no puede estar vacío" }
        repository.renameTrainingPlan(planId, trimmedName)
    }
}
