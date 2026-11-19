package com.pad.repitdoit.features.training.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.core.ui.common.textPopup
import com.pad.repitdoit.features.training.presentation.action.TrainingAction
import com.pad.repitdoit.features.training.presentation.components.TrainingDialog
import com.pad.repitdoit.features.training.presentation.state.TrainingState
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseEditState
import com.pad.repitdoit.features.training.presentation.screen.TrainingExerciseDetailScreen
import com.pad.repitdoit.features.training.presentation.screen.TrainingOverviewScreen
import com.pad.repitdoit.features.training.presentation.screen.TrainingWorkoutDetailScreen
import com.pad.repitdoit.features.training.presentation.screen.PlanSelectionScreen
import com.pad.repitdoit.features.training.presentation.screen.PlanDetailScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Una clave String permite que el navegador conserve la selección en todas las plataformas.
@Serializable
private sealed interface TrainingDetailDestination {
    @Serializable
    data object PlanSelection : TrainingDetailDestination

    @Serializable
    data class PlanDayDetail(val planId: String, val weekday: Int) : TrainingDetailDestination

    @Serializable
    data class WorkoutDetail(val workoutId: String) : TrainingDetailDestination

    @Serializable
    data class ExerciseDetail(
        val workoutId: String,
        val exerciseId: String
    ) : TrainingDetailDestination
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable

fun TrainingLayout(
    state: TrainingState,
    initialWorkoutId: String? = null,
    onAction: (TrainingAction) -> Unit,
    onStartWorkout: (workoutId: String) -> Unit,
    onAddExercise: (workoutId: String) -> Unit
) {
    var activeDialog by remember { mutableStateOf<TrainingDialog?>(null) }
    val onShowDialog: (TrainingDialog) -> Unit = { dialog ->
        if (activeDialog == null) {
            activeDialog = dialog
        }
    }

    // One single dialog popup through both panels
    key(activeDialog) {
        when (val dialog = activeDialog) {
            is TrainingDialog.RemoveWorkout -> {
                val workoutName = state.workouts.find { it.id == dialog.workoutId }?.name
                AlertDialog(
                    onDismissRequest = { activeDialog = null },
                    title = { Text("Eliminar entrenamiento") },
                    text = {
                        val subject = workoutName?.let { "el entrenamiento «$it»" } ?: "este entrenamiento"
                        Text("¿Quieres eliminar $subject? Dejará de aparecer en tus entrenamientos y planes semanales. Los ejercicios del catálogo se conservarán.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onAction(TrainingAction.RemoveWorkout(dialog.workoutId))
                            activeDialog = null
                        }) { Text("Eliminar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { activeDialog = null }) { Text("Cancelar") }
                    }
                )
            }
            is TrainingDialog.RemoveExerciseFromWorkout -> {
                val exerciseName = state.workouts.find { it.id == dialog.workoutId }
                    ?.workoutExercises?.find { it.id == dialog.workoutExerciseId }?.name
                AlertDialog(
                    onDismissRequest = { activeDialog = null },
                    title = { Text("Quitar ejercicio") },
                    text = {
                        Text(
                            if (exerciseName != null) {
                                "¿Quieres quitar «$exerciseName» de este entrenamiento? El ejercicio seguirá disponible en el catálogo."
                            } else {
                                "¿Quieres quitar este ejercicio del entrenamiento? Seguirá disponible en el catálogo."
                            }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onAction(
                                TrainingAction.removeExerciseFromWorkout(
                                    dialog.workoutId,
                                    dialog.workoutExerciseId
                                )
                            )
                            activeDialog = null
                        }) { Text("Quitar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { activeDialog = null }) { Text("Cancelar") }
                    }
                )
            }
            TrainingDialog.CreatePlan -> textPopup(
                title = "Crear plan",
                placeHolderText = "Nombre del plan",
                onConfirmAction = { name ->
                    onAction(TrainingAction.CreatePlan(name))
                    activeDialog = null
                },
                onCancelAction = { activeDialog = null }
            )
            is TrainingDialog.DeletePlan -> AlertDialog(
                onDismissRequest = { activeDialog = null },
                title = { Text("Eliminar plan") },
                text = { Text("Se eliminará el plan y su distribución semanal. Los entrenamientos se conservarán.") },
                confirmButton = {
                    TextButton(onClick = {
                        onAction(TrainingAction.DeletePlan(dialog.planId))
                        activeDialog = null
                    }) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { activeDialog = null }) { Text("Cancelar") }
                }
            )
            is TrainingDialog.AddWorkoutToPlan -> AlertDialog(
                onDismissRequest = { activeDialog = null },
                title = { Text("Añadir entrenamiento al día") },
                text = {
                    if (state.workouts.isEmpty()) {
                        Text("No tienes entrenamientos disponibles. Crea uno desde Entrenamientos.")
                    } else {
                        LazyColumn(Modifier.heightIn(max = 400.dp)) {
                            items(state.workouts, key = { it.id }) { workout ->
                                TextButton(onClick = {
                                    onAction(TrainingAction.AddWorkoutToPlan(dialog.planId, workout.id, dialog.weekday))
                                    activeDialog = null
                                }) { Text(workout.name) }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { activeDialog = null }) { Text("Cancelar") }
                }
            )
            TrainingDialog.CreateWorkout -> textPopup(
                title = "Añadir entrenamiento",
                placeHolderText = "Nombre del entrenamiento",
                onConfirmAction = { name ->
                    onAction(TrainingAction.createWorkout(name))
                    activeDialog = null
                },
                onCancelAction = { activeDialog = null }
            )

            is TrainingDialog.RenameWorkout -> textPopup(
                title = "Renombrar entrenamiento",
                placeHolderText = "Nuevo nombre",
                onConfirmAction = { name ->
                    onAction(TrainingAction.renameWorkout(dialog.workoutId, name))
                    activeDialog = null
                },
                onCancelAction = { activeDialog = null }
            )

            is TrainingDialog.RenamePlan -> textPopup(
                title = "Renombrar plan",
                placeHolderText = "Nuevo nombre",
                onConfirmAction = { name ->
                    onAction(TrainingAction.RenamePlan(dialog.planId, name))
                    activeDialog = null
                },
                onCancelAction = { activeDialog = null }
            )

            null -> Unit
        }
    }

    // Los tamaños de la app deciden cuándo mostrar ambos paneles, incluido Medium.

    val initialHistory = remember(initialWorkoutId) {
        buildList<ThreePaneScaffoldDestinationItem<String>> {
            add(
                ThreePaneScaffoldDestinationItem(
                    pane = ListDetailPaneScaffoldRole.List,
                    contentKey = null
                )
            )

            initialWorkoutId?.let { workoutId ->
                add(
                    ThreePaneScaffoldDestinationItem(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        contentKey = Json.encodeToString<TrainingDetailDestination>(
                            TrainingDetailDestination.WorkoutDetail(
                                workoutId = workoutId
                            )
                        )
                    )
                )
            }
        }
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<String>(
        initialDestinationHistory = initialHistory
    )

    // Una única posición central y sin tirador: el reparto siempre es 50/50.
    val paneExpansionState = rememberPaneExpansionState(
        anchors = listOf(PaneExpansionAnchor.Proportion(0.5f)),
        initialAnchoredIndex = 0
    )
    val scope = rememberCoroutineScope()
    val destination = navigator.currentDestination?.contentKey?.let { key ->
        Json.decodeFromString<TrainingDetailDestination>(key)
    }
    val selectedWorkoutId = when (destination) {
        is TrainingDetailDestination.WorkoutDetail -> destination.workoutId
        is TrainingDetailDestination.ExerciseDetail -> destination.workoutId
        else -> null
    }
    val navigateToDetail: (TrainingDetailDestination) -> Unit = { target ->
        val key = Json.encodeToString<TrainingDetailDestination>(target)
        if (navigator.currentDestination?.contentKey != key) {
            scope.launch {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    contentKey = key
                )
            }
        }
    }

    var lastDetailDestination by remember {
        mutableStateOf<TrainingDetailDestination?>(destination)
    }
    SideEffect {
        if (destination != null) {
            lastDetailDestination = destination
        }
    }
    val backBehavior = BackNavigationBehavior.PopUntilContentChange
    val onBack: () -> Unit = {
        scope.launch { navigator.navigateBack(backBehavior) }
    }
    OnBackHandler(
        enabled = navigator.canNavigateBack(backBehavior),
        onBack = onBack
    )

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        paneExpansionState = paneExpansionState,
        listPane = {
            AnimatedPane {
                TrainingOverviewScreen(
                    state = state,
                    selectedWorkoutId = selectedWorkoutId,
                    onChoosePlan = {
                        navigateToDetail(TrainingDetailDestination.PlanSelection)
                    },
                    onPlanDayDetails = { planId, weekday ->
                        navigateToDetail(TrainingDetailDestination.PlanDayDetail(planId, weekday))
                    },
                    onShowDialog = onShowDialog,
                    onWorkoutDetails = { workoutId ->
                        navigateToDetail(TrainingDetailDestination.WorkoutDetail(workoutId))
                    },
                    onStartWorkout = onStartWorkout
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedDestination =
                    if (navigator.scaffoldDirective.maxHorizontalPartitions == 1) {
                        destination ?: lastDetailDestination
                    } else {
                        destination
                    }

                when (selectedDestination) {
                    null -> TrainingDetailPlaceholder()
                    TrainingDetailDestination.PlanSelection -> {
                        PlanSelectionScreen(
                            state = state,
                            onSelectPlan = { planId ->
                                onAction(TrainingAction.SelectPlan(planId))
                                onBack()
                            },
                            onShowDialog = onShowDialog,
                            onBack = onBack
                        )
                    }
                    is TrainingDetailDestination.PlanDayDetail -> {
                        val plan = state.plans.find { it.id == selectedDestination.planId }
                        when {
                            plan != null -> PlanDetailScreen(
                                plan = plan,
                                weekday = selectedDestination.weekday,
                                onAction = onAction,
                                onShowDialog = onShowDialog,
                                onWorkoutDetails = { workoutId ->
                                    navigateToDetail(TrainingDetailDestination.WorkoutDetail(workoutId))
                                },
                                onBack = onBack,
                                errorMessage = state.errorMessage,
                                isSaving = state.isSavingPlan
                            )
                            state.isLoadingPlans -> androidx.compose.material3.CircularProgressIndicator()
                            else -> TrainingUnavailableScreen(onBack)
                        }
                    }
                    is TrainingDetailDestination.WorkoutDetail -> {
                        val workout = state.workouts.find {
                            it.id == selectedDestination.workoutId
                        }

                        if (workout == null) {
                            TrainingUnavailableScreen(onBack)
                        } else {
                            TrainingWorkoutDetailScreen(
                                workout = workout,
                                onShowDialog = onShowDialog,
                                onExercise = { selectedExerciseId ->
                                    navigateToDetail(
                                        TrainingDetailDestination.ExerciseDetail(
                                            workoutId = selectedDestination.workoutId,
                                            exerciseId = selectedExerciseId
                                        )
                                    )

                                },
                                onBack = onBack,
                                onDeleteExercise = { workoutExerciseId ->
                                    onShowDialog(
                                        TrainingDialog.RemoveExerciseFromWorkout(workout.id, workoutExerciseId)
                                    )
                                },
                                onAddExercise = { onAddExercise(workout.id) },
                                onStart = { onStartWorkout(workout.id) },
                            )
                        }
                    }
                    is TrainingDetailDestination.ExerciseDetail -> {
                        val workout = state.workouts.find {
                            it.id == selectedDestination.workoutId
                        }
                        if (workout == null) {
                            TrainingUnavailableScreen(onBack)
                        } else {
                            val exerciseId = selectedDestination.exerciseId
                            val exercise = workout.workoutExercises.find {
                                it.id == exerciseId
                            }

                            if (exercise != null) {
                                LaunchedEffect(exercise.id) {
                                    onAction(TrainingAction.SelectExercise(exercise.id))
                                }
                                TrainingExerciseDetailScreen(
                                    exercise = exercise,
                                    editState = if (state.editingExerciseId == exercise.id)
                                        state.exerciseEdit else WorkoutExerciseEditState(),
                                    onAction = onAction,
                                    onBack = onBack
                                )
                            } else {
                                TrainingUnavailableScreen(onBack)
                            }
                        }
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
