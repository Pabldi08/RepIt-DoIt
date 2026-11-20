package com.pad.repitdoit.features.catalogue.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.features.catalogue.presentation.CatalogueAction
import com.pad.repitdoit.features.catalogue.presentation.CatalogueState

// Presenta la búsqueda de ejercicios asociados a máquinas.
@Composable
fun MachineSearchScreen(
    state: CatalogueState,
    selectedExerciseId: String?,
    onAction: (CatalogueAction) -> Unit,
    onExerciseDetails: (exerciseId: String) -> Unit,
    onBack: () -> Unit
) {
    CatalogueSearchContent(
        title = "Ejercicios de la máquina",
        state = state,
        selectedExerciseId = selectedExerciseId,
        onAction = onAction,
        onExerciseDetails = onExerciseDetails,
        onBack = onBack
    )
}

