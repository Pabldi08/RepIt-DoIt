package com.pad.repitdoit.features.catalogue.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.catalogue.domain.model.Exercise
import com.pad.repitdoit.features.catalogue.domain.repository.CatalogueRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CatalogueRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : CatalogueRepository {
    override suspend fun getExercises(): List<Exercise> = withContext(dispatcher) {
        db.exerciseQueries.selectAllExercises().executeAsList().map { exercise ->
            Exercise(exercise.id, exercise.name, exercise.description)
        }
    }

    override suspend fun getExercisesByMachine(machineId: String): List<Exercise> {
        TODO("Not yet implemented")
    }
}
