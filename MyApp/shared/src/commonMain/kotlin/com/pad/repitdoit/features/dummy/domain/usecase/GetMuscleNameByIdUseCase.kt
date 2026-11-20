package com.pad.repitdoit.features.dummy.domain.usecase

import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository

// Resuelve el nombre de un músculo a partir de su identificador.
class GetMuscleNameByIdUseCase (
    private val repository: DummyRepository
){
    suspend operator fun invoke(muscleId: String): String {
        return repository.getMuscleNameById(muscleId)
    }
}