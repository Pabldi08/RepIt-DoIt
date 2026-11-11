package com.pad.repitdoit.features.catalogue.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.di.CatalogueUseCasesAC
import com.pad.repitdoit.features.catalogue.presentation.CatalogueViewModel
import com.pad.repitdoit.features.catalogue.presentation.layout.CatalogueLayout

@Composable
fun CatalogueScreenRoot(
    appContainer: CatalogueUseCasesAC,
    machineId: String?,
    onBack: () -> Unit,
    onAddExercise: (exerciseId: String) -> Unit
) {
    val viewModel: CatalogueViewModel = viewModel {
        CatalogueViewModel(
            appContainer = appContainer,
            machineId = machineId
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogueLayout(
        state = uiState,
        machineId = machineId,
        onAction = viewModel::onAction,
        onBack = onBack,
        onAddExercise = onAddExercise
    )
}
