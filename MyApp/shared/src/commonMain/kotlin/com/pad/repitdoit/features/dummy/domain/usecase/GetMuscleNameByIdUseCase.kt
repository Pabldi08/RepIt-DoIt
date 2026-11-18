package com.pad.repitdoit.features.dummy.domain.usecase

import com.pad.repitdoit.features.dummy.domain.model.Exercise
import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository

class GetMuscleNameByIdUseCase (
    private val repository: DummyRepository
){
    suspend operator fun invoke(muscleId: String): String {
        return repository.getMuscleNameById(muscleId)
    }
}