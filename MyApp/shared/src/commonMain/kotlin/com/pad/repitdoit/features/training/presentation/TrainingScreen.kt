package com.pad.repitdoit.features.training.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.core.ui.common.Header

@Composable
internal fun TrainingOverviewScreen(
    windowSize: AppWindowSize,
    onWorkoutDetails: (String) -> Unit,
    plan: TrainingPlanPreview = TrainingPreviewData.plan,
    singleColumn: Boolean = false,
    selectedWorkoutId: String? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (!singleColumn && windowSize == AppWindowSize.Extended) 2 else 1),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { Header(Modifier.fillMaxWidth().height(40.dp)) }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("RUTINA SELECCIONADA", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(plan.name, style = MaterialTheme.typography.headlineMedium)
                Text("${plan.days.count { it.workoutId != null }} entrenamientos · ${plan.days.count { it.workoutId == null }} días de descanso", style = MaterialTheme.typography.bodyMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { /* Pendiente de conectar */ }) { Text("Elegir rutina") }
                    OutlinedButton(onClick = { /* Pendiente de conectar */ }) { Text("Personalizar") }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(plan.days) { day ->
                    val workout = plan.workouts.find { it.id == day.workoutId }
                    OutlinedCard(
                        onClick = { workout?.let { onWorkoutDetails(it.id) } },
                        enabled = workout != null,
                        modifier = Modifier.width(148.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(day.label, style = MaterialTheme.typography.labelLarge,
                                color = if (workout != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(workout?.title ?: "Descanso", style = MaterialTheme.typography.titleSmall)
                            Text(workout?.let { "${it.minutes} min · ${it.exercises.size} ejercicios" } ?: "Recupera energías",
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Entrenamientos", style = MaterialTheme.typography.titleLarge)
            }
        }
        items(plan.workouts, key = { it.id }) { workout ->
            TrainingCard(
                title = workout.title, day = workout.day, image = workout.image, xp = workout.xp,
                isSelected = workout.id == selectedWorkoutId,
                exercises = workout.exercises.map { ExerciseItem(it.name, "${it.sets.size} × ${it.sets.first().reps} repeticiones · ${it.sets.first().weightKg} kg") },
                onDetails = { onWorkoutDetails(workout.id) },
                onStart = { /* Pendiente de conectar */ }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedButton(onClick = { /* Pendiente de conectar */ }, modifier = Modifier.fillMaxWidth()) {
                Text("+ Añadir entrenamiento")
            }
        }
    }
}
