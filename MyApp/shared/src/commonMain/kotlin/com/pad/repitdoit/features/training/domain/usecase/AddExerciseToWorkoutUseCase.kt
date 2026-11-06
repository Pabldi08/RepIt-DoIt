package com.pad.repitdoit.features.training.domain.usecase

import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository

class AddExerciseToWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int,
        weight: Double = 0.0,
        restSec: Int = 0
    ): WorkoutExercise {
        require(workoutId.isNotBlank()) { "El identificador de la rutina no puede estar vacio" }
        require(exerciseId.isNotBlank()) { "El identificador del ejercicio no puede estar vacio" }
        require(numReps > 0) { "Las repeticiones deben ser mayores que cero" }
        require(numSeries > 0) { "Las series deben ser mayores que cero" }
        require(weight.isFinite() && weight >= 0.0) { "El peso debe ser finito y no negativo" }
        require(restSec >= 0) { "El descanso no puede ser negativo" }
        return repository.addWorkoutExercise(workoutId, exerciseId, numReps, numSeries, weight, restSec)
    }
}
