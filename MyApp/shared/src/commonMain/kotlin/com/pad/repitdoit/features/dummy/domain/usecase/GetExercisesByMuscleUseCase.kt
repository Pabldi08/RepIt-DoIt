package com.pad.repitdoit.features.dummy.domain.usecase

import com.pad.repitdoit.features.dummy.domain.model.Exercise
import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository

class GetExercisesByMuscleUseCase (
    private val repository: DummyRepository
){
    suspend operator fun invoke(muscleId: String): List<Exercise> {
        return repository.getExerciseByMuscle(muscleId)
    }
}