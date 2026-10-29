package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.core.ui.common.Header
import com.pad.repitdoit.core.ui.common.textPopup
import com.pad.repitdoit.features.training.presentation.components.ExerciseItem
import com.pad.repitdoit.features.training.presentation.components.TrainingCard
import com.pad.repitdoit.features.training.presentation.TrainingPlanPreview
import com.pad.repitdoit.features.training.presentation.TrainingPreviewData
import com.pad.repitdoit.features.training.presentation.TrainingUIState

@Composable
internal fun TrainingOverviewScreen(
    windowSize: AppWindowSize,
    plan: TrainingPlanPreview = TrainingPreviewData.plan,
    state: TrainingUIState,
    singleColumn: Boolean = false,
    selectedWorkoutId: String? = null,
    onCreateWorkout: (String) -> Unit,
    onWorkoutDetails: (String) -> Unit
) {
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    if (showCreateDialog) {
        textPopup(
            title = "Añadir entrenamiento",
            placeHolderText = "Nombre del entrenamiento",
            onConfirmAction = onCreateWorkout,
            onCancelAction = { showCreateDialog = false }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(if (!singleColumn && windowSize == AppWindowSize.Extended) 2 else 1),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Header(Modifier.fillMaxWidth().height(40.dp))
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "RUTINA SELECCIONADA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = plan.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${plan.days.count { it.workoutId != null }} entrenamientos · ${plan.days.count { it.workoutId == null }} días de descanso",
                    style = MaterialTheme.typography.bodyMedium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Pendiente de conectar */ }
                    ) { Text("Elegir rutina") }
                    OutlinedButton(
                        onClick = { /* Pendiente de conectar */ }
                    ) { Text("Personalizar") }
                }
            }
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(plan.days) { day ->
                    val workout = plan.workouts.find { it.id == day.workoutId }
                    OutlinedCard(
                        onClick = { workout?.let { onWorkoutDetails(it.id) } },
                        enabled = workout != null,
                        modifier = Modifier.width(148.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = day.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (workout != null) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = workout?.title ?: "Descanso",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = workout?.let { "${it.minutes} min · ${it.exercises.size} ejercicios" } ?: "Recupera energías",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Entrenamientos", style = MaterialTheme.typography.titleLarge)
            }
        }

        items(
            state.workouts,
            key = { it.id }
        ) { workout ->
            TrainingCard(
                title = workout.name,
                exercises = workout.workoutExercises,
                image = workout.image,
                xp = workout.workoutExercises.size * 50,
                isSelected = workout.id == selectedWorkoutId,
                onDetails = { onWorkoutDetails(workout.id) },
                onStart = { /* Pendiente de conectar */ }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedButton(
                onClick = {showCreateDialog = true},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Añadir entrenamiento")
            }
        }
    }
}
