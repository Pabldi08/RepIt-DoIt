package com.pad.repitdoit.features.catalogue.domain.usecase

import com.pad.repitdoit.features.catalogue.domain.model.Exercise
import com.pad.repitdoit.features.catalogue.domain.repository.CatalogueRepository

// Consulta los ejercicios asociados a una máquina.
class GetExercisesByMachineUseCase(
    private val repository: CatalogueRepository
) {
    suspend operator fun invoke(
        machineId: String
    ) : List<Exercise> {
        return repository.getExercisesByMachine(machineId)
    }
}