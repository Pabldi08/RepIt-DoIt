package com.pad.repitdoit.features.dummy.domain.repository

import com.pad.repitdoit.features.dummy.domain.model.Exercise

interface DummyRepository {
    suspend fun getExerciseByMuscle(muscleId: String): List<Exercise>
    suspend fun getMuscleNameById(muscleId: String): String
}