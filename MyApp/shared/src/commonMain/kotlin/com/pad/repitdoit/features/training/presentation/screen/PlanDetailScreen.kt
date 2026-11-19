package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.ReturnButton
import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.presentation.action.TrainingAction
import com.pad.repitdoit.features.training.presentation.components.TrainingDialog

@Composable
fun PlanDetailScreen(
    plan: TrainingPlan,
    weekday: Int,
    onAction: (TrainingAction) -> Unit,
    onShowDialog: (TrainingDialog) -> Unit,
    onWorkoutDetails: (String) -> Unit,
    onBack: () -> Unit,
    errorMessage: String? = null,
    isSaving: Boolean = false
) {
    require(weekday in 1..7) { "El día debe estar entre 1 (lunes) y 7 (domingo)" }
    val dayNames = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val entries = plan.schedule.filter { it.weekday == weekday }.sortedBy { it.position }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ReturnButton(onBack) }
        item { Text(plan.name, style = MaterialTheme.typography.headlineMedium) }
        item {
            Text(dayNames[weekday - 1], style = MaterialTheme.typography.headlineSmall)
        }
        errorMessage?.let { message ->
            item { Text(message, color = MaterialTheme.colorScheme.error) }
        }
        if (isSaving) {
            item { Text("Guardando cambios…") }
        }
            item(key = "day-$weekday") {
                Column {
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("Entrenamientos del día", style = MaterialTheme.typography.titleMedium)
                        TextButton(enabled = !isSaving, onClick = {
                            onShowDialog(TrainingDialog.AddWorkoutToPlan(plan.id, weekday))
                        }) { Text("Añadir entrenamiento") }
                    }
                    if (entries.isEmpty()) {
                        Text(
                            "No hay entrenamientos programados para este día. Puedes añadir uno.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            items(entries, key = { it.id }) { entry ->
                OutlinedCard(
                    onClick = { onWorkoutDetails(entry.workoutId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(entry.workoutName, style = MaterialTheme.typography.titleMedium)
                        Text("${entry.numEx} ejercicios")
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { onWorkoutDetails(entry.workoutId) }) { Text("Ver entrenamiento") }
                            TextButton(enabled = !isSaving, onClick = {
                                onAction(TrainingAction.RemoveWorkoutFromPlan(plan.id, entry.id))
                            }) { Text("Quitar de este día", color = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }
    }
}
