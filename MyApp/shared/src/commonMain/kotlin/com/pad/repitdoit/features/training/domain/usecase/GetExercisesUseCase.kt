package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.Exercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class GetExercisesUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
