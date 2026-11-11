package com.pad.repitdoit.features.catalogue.domain.repository

import com.pad.repitdoit.features.catalogue.domain.model.Exercise

interface CatalogueRepository {
    suspend fun getExercises(): List<Exercise>
    suspend fun getExercisesByMachine(
        machineId: String
    ) : List<Exercise>
}
