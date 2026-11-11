package com.pad.repitdoit.features.catalogue.presentation

import com.pad.repitdoit.features.catalogue.domain.model.Exercise

data class CatalogueState(
    val exercises: List<Exercise> = emptyList(),
    val query: String = "",
    val errorMessage: String? = null
) {
    val filteredExercises: List<Exercise>
        get() = exercises.filter {
            it.name.contains(
                query.trim(),
                ignoreCase = true
            )
        }

}
