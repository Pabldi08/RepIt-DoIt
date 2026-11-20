package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Asigna un entrenamiento a un día del plan.
class AddWorkoutToPlanUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String, workoutId: String, weekday: Int) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        require(workoutId.isNotBlank()) { "El identificador del entrenamiento no puede estar vacío" }
        require(weekday in 1..7) { "El día debe estar entre 1 y 7" }
        repository.addWorkoutToPlan(planId, workoutId, weekday)
    }
}
