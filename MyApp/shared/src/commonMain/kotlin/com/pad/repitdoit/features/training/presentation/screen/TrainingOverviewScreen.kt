package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.Header
import com.pad.repitdoit.features.training.presentation.TrainingDialog
import com.pad.repitdoit.features.training.presentation.components.TrainingCard
import com.pad.repitdoit.features.training.presentation.TrainingPlanPreview
import com.pad.repitdoit.features.training.presentation.TrainingPreviewData
import com.pad.repitdoit.features.training.presentation.TrainingState

@Composable
internal fun TrainingOverviewScreen(
    plan: TrainingPlanPreview = TrainingPreviewData.plan,
    state: TrainingState,
    selectedWorkoutId: String? = null,
    onShowDialog: (TrainingDialog) -> Unit,
    onWorkoutDetails: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Entrenamientos",
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(
                    onClick = { onShowDialog(TrainingDialog.CreateWorkout) }
                ) {
                    Text(
                        text = "+ Crear"
                    )
                }
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
                onClick = { onShowDialog(TrainingDialog.CreateWorkout) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Añadir entrenamiento")
            }
        }
    }
}
