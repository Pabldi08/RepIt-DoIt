package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Registra una sesión de entrenamiento completada.
class CompleteWorkoutSessionUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(sessionId: String, workout: Workout, startedAt: Long, finishedAt: Long) {
        repository.completeWorkoutSession(sessionId, workout, startedAt, finishedAt)
    }
}
