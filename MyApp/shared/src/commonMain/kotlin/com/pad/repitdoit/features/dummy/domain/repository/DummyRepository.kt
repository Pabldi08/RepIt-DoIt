package com.pad.repitdoit.features.dummy.domain.repository

import com.pad.repitdoit.features.dummy.domain.model.Exercise

// Define las operaciones para consultar o gestionar los ejercicios y músculos del módulo de ejemplo.
interface DummyRepository {
    suspend fun getExerciseByMuscle(muscleId: String): List<Exercise>
    suspend fun getMuscleNameById(muscleId: String): String
}