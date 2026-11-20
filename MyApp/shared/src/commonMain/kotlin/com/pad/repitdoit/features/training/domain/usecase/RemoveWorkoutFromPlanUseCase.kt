package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Quita una sesión programada de un plan.
class RemoveWorkoutFromPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String, scheduleId: String) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        require(scheduleId.isNotBlank()) { "El identificador de la asignación no puede estar vacío" }
        repository.removeWorkoutFromPlan(planId, scheduleId)
    }
}
