package com.pad.repitdoit.features.catalogue.presentation.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.features.catalogue.presentation.CatalogueAction
import com.pad.repitdoit.features.catalogue.presentation.CatalogueState
import com.pad.repitdoit.features.catalogue.presentation.screen.ExerciseDetailScreen
import com.pad.repitdoit.features.catalogue.presentation.screen.ExerciseSearchScreen
import com.pad.repitdoit.features.catalogue.presentation.screen.MachineSearchScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CatalogueLayout(
    state: CatalogueState,
    machineId: String?,
    onAction: (CatalogueAction) -> Unit,
    onBack: () -> Unit,
    onAddExercise: (exerciseId: String) -> Unit
) {
    val initialHistory = remember {
        listOf(
            ThreePaneScaffoldDestinationItem<String>(
                pane = ListDetailPaneScaffoldRole.List,
                contentKey = null
            )
        )
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<String>(
        initialDestinationHistory = initialHistory
    )
    val paneExpansionState = rememberPaneExpansionState(
        anchors = listOf(PaneExpansionAnchor.Proportion(0.5f)),
        initialAnchoredIndex = 0
    )
    val scope = rememberCoroutineScope()
    // Solo se necesita el ID del ejercicio, sin una estructura de destino auxiliar.
    val destination = navigator.currentDestination?.contentKey
    var lastDetailExerciseId by remember { mutableStateOf(destination) }
    SideEffect {
        if (destination != null) lastDetailExerciseId = destination
    }

    val backBehavior = BackNavigationBehavior.PopUntilContentChange
    val canNavigateBack = navigator.canNavigateBack(backBehavior)
    val onDetailBack: () -> Unit = {
        if (canNavigateBack) {
            scope.launch { navigator.navigateBack(backBehavior) }
        } else {
            onBack()
        }
    }
    OnBackHandler(enabled = canNavigateBack, onBack = onDetailBack)

    val onExerciseDetails: (String) -> Unit = { exerciseId ->
        if (navigator.currentDestination?.contentKey != exerciseId) {
            scope.launch {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    contentKey = exerciseId
                )
            }
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        paneExpansionState = paneExpansionState,
        listPane = {
            AnimatedPane {
                if (machineId == null) {
                    ExerciseSearchScreen(
                        state = state,
                        selectedExerciseId = destination,
                        onAction = onAction,
                        onExerciseDetails = onExerciseDetails,
                        onBack = onBack
                    )
                } else {
                    MachineSearchScreen(
                        state = state,
                        selectedExerciseId = destination,
                        onAction = onAction,
                        onExerciseDetails = onExerciseDetails,
                        onBack = onBack
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                // Mantiene el contenido saliente durante la animación, como Training.
                val selectedExerciseId =
                    if (navigator.scaffoldDirective.maxHorizontalPartitions == 1) {
                        destination ?: lastDetailExerciseId
                    } else {
                        destination
                    }
                if (selectedExerciseId == null) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Selecciona un ejercicio para ver su información.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    val exercise = state.exercises.find { it.id == selectedExerciseId }
                    if (exercise != null) {
                        ExerciseDetailScreen(
                            exercise = exercise,
                            onBack = onDetailBack,
                            onAddExercise = { onAddExercise(exercise.id) }
                        )
                    } else {
                        Column(Modifier.padding(24.dp)) {
                            Text("Ejercicio no disponible")
                            TextButton(onClick = onDetailBack) { Text("Volver") }
                        }
                    }
                }
            }
        }
    )
}

