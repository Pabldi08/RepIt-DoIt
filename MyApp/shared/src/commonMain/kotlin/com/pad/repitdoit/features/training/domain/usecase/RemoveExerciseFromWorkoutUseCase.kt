package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

// Quita un ejercicio de un entrenamiento.
class RemoveExerciseFromWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String, workoutExerciseId: String) {
        require(workoutId.isNotBlank()) { "El identificador del entrenamiento no puede estar vacío" }
        require(workoutExerciseId.isNotBlank()) { "El identificador de la entrada no puede estar vacio" }
        repository.removeWorkoutExercise(workoutId, workoutExerciseId)
    }
}
