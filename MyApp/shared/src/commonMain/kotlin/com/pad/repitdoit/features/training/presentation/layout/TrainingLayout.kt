package com.pad.repitdoit.features.training.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.features.training.presentation.screen.TrainingExerciseDetailScreen
import com.pad.repitdoit.features.training.presentation.screen.TrainingOverviewScreen
import com.pad.repitdoit.features.training.presentation.screen.TrainingWorkoutDetailScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Una clave String permite que el navegador conserve la selección en todas las plataformas.
// El ID del ejercicio solo aparece cuando el panel de detalle muestra sus series.
@Serializable
private data class TrainingDetailDestination(
    val planId: String,
    val workoutId: String,
    val exerciseId: String? = null
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TrainingScreen(windowSize: AppWindowSize, onWorkoutDetails: (String) -> Unit) {
    val plan = _root_ide_package_.com.pad.repitdoit.features.training.presentation.TrainingPreviewData.plan
    // Los tamaños de la app deciden cuándo mostrar ambos paneles, incluido Medium.
    val directive = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo()).copy(
        maxHorizontalPartitions = if (windowSize == AppWindowSize.Compact) 1 else 2,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = 1
    )
    val navigator = rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective = directive)
    // Una única posición central y sin tirador: el reparto siempre es 50/50.
    val paneExpansionState = rememberPaneExpansionState(
        anchors = listOf(PaneExpansionAnchor.Proportion(0.5f)),
        initialAnchoredIndex = 0
    )
    val scope = rememberCoroutineScope()
    val destination = navigator.currentDestination?.contentKey?.let { key ->
        Json.decodeFromString<TrainingDetailDestination>(key)
    }
    val workout = plan.workouts.find { it.id == destination?.workoutId }
    val backBehavior = BackNavigationBehavior.PopUntilContentChange
    val onBack: () -> Unit = {
        scope.launch { navigator.navigateBack(backBehavior) }
    }
    OnBackHandler(enabled = navigator.canNavigateBack(backBehavior), onBack = onBack)

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        paneExpansionState = paneExpansionState,
        listPane = {
            AnimatedPane {
                TrainingOverviewScreen(
                    windowSize = windowSize,
                    singleColumn = true,
                    selectedWorkoutId = workout?.id,
                    onWorkoutDetails = { id ->
                        if (_root_ide_package_.com.pad.repitdoit.features.training.presentation.TrainingPreviewData.workout(id) != null) {
                            val key = Json.encodeToString(TrainingDetailDestination(plan.id, id))
                            if (navigator.currentDestination?.contentKey != key) {
                                scope.launch {
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        key
                                    )
                                }
                            }
                        } else onWorkoutDetails(id)
                    },
                    plan = plan
                )
            }
        },
        detailPane = {
            AnimatedPane {
                when {
                    destination == null -> TrainingDetailPlaceholder()
                    workout == null || destination.planId != plan.id -> TrainingUnavailableScreen(onBack)
                    destination.exerciseId == null -> TrainingWorkoutDetailScreen(
                        workout = workout,
                        onExercise = { exerciseId ->
                            scope.launch {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail,
                                    contentKey = Json.encodeToString(
                                        value = TrainingDetailDestination(
                                            planId = plan.id,
                                            workoutId = workout.id,
                                            exerciseId
                                        )
                                    )
                                )
                            }
                        },
                        onBack = onBack
                    )
                    else -> {
                        val exercise = workout.exercises.find { it.id == destination.exerciseId }
                        if (exercise != null) {
                            TrainingExerciseDetailScreen(workout, exercise, onBack)
                        } else TrainingUnavailableScreen(onBack)
                    }
                }
            }
        }
    )
}

@Composable
private fun TrainingDetailPlaceholder() {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona un entrenamiento para ver sus ejercicios y series.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun TrainingUnavailableScreen(onBack: () -> Unit) {
    Column {
        Text("Contenido no disponible")
        TextButton(onClick = onBack) { Text("Volver") }
    }
}
