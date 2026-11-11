package com.pad.repitdoit.features.catalogue.data.repository

import com.pad.repitdoit.features.catalogue.domain.model.Exercise
import com.pad.repitdoit.features.catalogue.domain.repository.CatalogueRepository

class FakeCatalogueRepositoryImpl : CatalogueRepository {
    private val exercises = listOf(
        Exercise("10", "Press de banca", null),
        Exercise("20", "Remo con mancuerna", null),
        Exercise("30", "Jalón al pecho", null),
        Exercise("40", "Curl de bíceps", null)
    ).sortedBy { it.name }

    // Identificadores de máquinas de prueba, independientes del catálogo real.
    private val exerciseIdsByMachine = mapOf(
        "machine-001" to setOf("10"),
        "machine-002" to setOf("20", "30", "40")
    )

    override suspend fun getExercises(): List<Exercise> = exercises

    override suspend fun getExercisesByMachine(machineId: String): List<Exercise> {
        val exerciseIds = exerciseIdsByMachine[machineId] ?: return emptyList()
        return exercises.filter { it.id in exerciseIds }
    }
}
