package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class MoveWorkoutToPlanDayUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(planId: String, scheduleId: String, weekday: Int) {
        require(planId.isNotBlank()) { "El identificador del plan no puede estar vacío" }
        require(scheduleId.isNotBlank()) { "El identificador de la asignación no puede estar vacío" }
        require(weekday in 1..7) { "El día debe estar entre 1 y 7" }
        repository.moveWorkoutToPlanDay(planId, scheduleId, weekday)
    }
}
