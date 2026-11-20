package com.pad.repitdoit.features.catalogue.domain.repository

import com.pad.repitdoit.features.catalogue.domain.model.Exercise

// Define las operaciones para consultar o gestionar los ejercicios del catálogo y sus asociaciones.
interface CatalogueRepository {
    suspend fun getExercises(): List<Exercise>
    suspend fun getExercisesByMachine(
        machineId: String
    ) : List<Exercise>
}
