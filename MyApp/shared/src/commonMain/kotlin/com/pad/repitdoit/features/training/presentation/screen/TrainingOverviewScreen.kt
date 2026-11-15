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
import com.pad.repitdoit.features.training.presentation.components.TrainingDialog
import com.pad.repitdoit.features.training.presentation.components.TrainingCard
import com.pad.repitdoit.features.training.presentation.state.TrainingState

@Composable
internal fun TrainingOverviewScreen(
    state: TrainingState,
    selectedWorkoutId: String? = null,
    onShowDialog: (TrainingDialog) -> Unit,
    onWorkoutDetails: (String) -> Unit,
    onChoosePlan: () -> Unit,
    onPlanDayDetails: (planId: String, weekday: Int) -> Unit,
    onStartWorkout: (String) -> Unit
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

        val selectedPlan = state.selectedPlan
        val scheduleByDay = selectedPlan
            ?.schedule
            .orEmpty()
            .groupBy { it.weekday }
        val trainingDays = selectedPlan
            ?.schedule
            .orEmpty()
            .map {it.weekday}
            .distinct()
            .size

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
                    text = selectedPlan?.name
                        ?: "Ningún plan seleccionado",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = if (selectedPlan == null) {
                        "Selecciona un plan para ver su distribución semanal"
                    } else {
                        "$trainingDays días de entrenamiento · ${7 - trainingDays} días de descanso"

                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onChoosePlan
                    ) {
                        Text(
                            text = "Elegir plan"
                        )
                    }
                }
            }
        }

        val dayNames = listOf(
            "Lunes", "Martes", "Miércoles", "Jueves",
            "Viernes", "Sábado", "Domingo"
        )
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items((1..7).toList()) { weekday ->
                    val workouts = scheduleByDay[weekday]
                        .orEmpty()
                        .sortedBy { it.position }

                    val firstWorkout = workouts.firstOrNull()
                    val extraCount = workouts.size - 1
                    OutlinedCard(
                        enabled = selectedPlan != null,
                        onClick = {
                            selectedPlan?.let { plan ->
                                onPlanDayDetails(plan.id, weekday)
                            }
                        },
                        modifier = Modifier.width(148.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = dayNames[weekday - 1],
                                style = MaterialTheme.typography.labelLarge,
                                color = if (workouts.isNotEmpty()) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when {
                                    selectedPlan == null -> "—"
                                    firstWorkout == null -> "Descanso"
                                    extraCount > 0 -> "${firstWorkout.workoutName} + $extraCount"
                                    else -> firstWorkout.workoutName
                                },
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = when {
                                    selectedPlan == null -> " "
                                    firstWorkout == null -> "Recupera energías"
                                    else -> "${firstWorkout.numEx} ejercicios"
                                },
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
                onStart = { onStartWorkout(workout.id) },
                onRemove = { onShowDialog(TrainingDialog.RemoveWorkout(workout.id)) }
            )
        }
    }
}
