package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class RemoveExerciseFromWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String, workoutExerciseId: String) {
        require(workoutId.isNotBlank()) { "El identificador de la rutina no puede estar vacio" }
        require(workoutExerciseId.isNotBlank()) { "El identificador de la entrada no puede estar vacio" }
        repository.removeWorkoutExercise(workoutId, workoutExerciseId)
    }
}
