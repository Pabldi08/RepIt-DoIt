package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class UpdateWorkoutExerciseUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String, workoutExercise: WorkoutExercise) {
        require(workoutId.isNotBlank()) { "El identificador de la rutina no puede estar vacio" }
        require(workoutExercise.id.isNotBlank()) { "El identificador de la entrada no puede estar vacio" }
        require(workoutExercise.exerciseId.isNotBlank()) { "El identificador del ejercicio no puede estar vacio" }
        require(workoutExercise.numReps > 0) { "Las repeticiones deben ser mayores que cero" }
        require(workoutExercise.numSets > 0) { "Las series deben ser mayores que cero" }
        require(workoutExercise.weight.isFinite() && workoutExercise.weight >= 0.0) {
            "El peso debe ser finito y no negativo"
        }
        require(workoutExercise.restSec >= 0) { "El descanso no puede ser negativo" }
        repository.updateWorkoutExercise(workoutId, workoutExercise)
    }
}
