package com.pad.repitdoit.features.catalogue.domain.usecase

import com.pad.repitdoit.features.catalogue.domain.model.Exercise
import com.pad.repitdoit.features.catalogue.domain.repository.CatalogueRepository

// Consulta los ejercicios del catálogo.
class GetExercisesUseCase(
    private val repository: CatalogueRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
