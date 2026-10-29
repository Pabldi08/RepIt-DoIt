package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class RenameWorkoutUseCase (
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke (
        workoutId: String,
        name: String
    ) {
        val trimName = name.trim()
        require(trimName.isNotEmpty()) {
            "El nombre del entrenamiento no puede estar vacio"
        }

        repository.renameWorkout(workoutId, trimName)
    }
}