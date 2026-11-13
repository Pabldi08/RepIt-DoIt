package com.pad.repitdoit.features.dummy.data.repository

import com.pad.repitdoit.features.dummy.domain.model.Exercise
import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository

class FakeDummyRepositoryImpl : DummyRepository{
    override suspend fun getExerciseByMuscle(
        muscleId: String
    ): List<Exercise> {
        TODO("Not yet implemented")
    }

    override suspend fun getMuscleNameById(
        muscleId: String
    ): String {
        TODO("Not yet implemented")
    }
}