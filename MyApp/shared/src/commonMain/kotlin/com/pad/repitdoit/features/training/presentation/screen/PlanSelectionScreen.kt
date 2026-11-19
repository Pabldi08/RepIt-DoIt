package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.ReturnButton
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.training.presentation.components.TrainingDialog
import com.pad.repitdoit.features.training.presentation.state.TrainingState
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlanSelectionScreen(
    state: TrainingState,
    onSelectPlan: (String) -> Unit,
    onShowDialog: (TrainingDialog) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ReturnButton(onBack)
        }

        item {
            Text("Elegir plan", style = MaterialTheme.typography.headlineMedium)
            Text("Selecciona el plan que quieres ver en tu semana.")
        }

        item {
            OutlinedButton(
                enabled = !state.isSavingPlan,
                onClick = { onShowDialog(TrainingDialog.CreatePlan) }
            ) {
                Text("Crear plan")
            }
        }

        if (state.isLoadingPlans) {
            item {
                CircularProgressIndicator()
            }
        }

        if (state.isSavingPlan) {
            item {
                Text("Guardando cambios…")
            }
        }

        state.plansError?.let { message ->
            item {
                Text(message, color = MaterialTheme.colorScheme.error)
            }
        }

        state.errorMessage?.let { message ->
            item {
                Text(message, color = MaterialTheme.colorScheme.error)
            }
        }

        if (!state.isLoadingPlans && state.plansError == null && state.plans.isEmpty()) {
            item {
                Text("Todavía no tienes planes. Crea uno para organizar tus entrenamientos.")
            }
        }

        items(state.plans, key = { it.id }) { plan ->
            val selected = plan.id == state.selectedPlanId
            OutlinedCard(
                enabled = !state.isSavingPlan,
                onClick = { onSelectPlan(plan.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            plan.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        TextButton(
                            enabled = !state.isSavingPlan,
                            onClick = { onShowDialog(TrainingDialog.RenamePlan(plan.id)) }
                        ) {
                            Icon (
                                painterResource(loadImage("icon_edit")),
                                contentDescription = "Edit button"
                            )
                        }
                    }
                    val days = plan.schedule.map { it.weekday }.distinct().size

                    Text(
                        "$days días de entrenamiento · ${7 - days} días de descanso"
                    )

                    if (selected) {
                        Text("Seleccionado", color = MaterialTheme.colorScheme.primary)
                    }

                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        TextButton(enabled = !state.isSavingPlan, onClick = { onShowDialog(TrainingDialog.RenamePlan(plan.id)) }) {
//                            Text("Renombrar")
//                        }
                        Button(
                            enabled = !state.isSavingPlan,
                            onClick = { onShowDialog(TrainingDialog.DeletePlan(plan.id)) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                painter = painterResource(loadImage("iconTrash")),
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}
