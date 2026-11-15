package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.training.presentation.action.WorkoutExerciseSetupAction
import com.pad.repitdoit.features.training.presentation.components.FormField
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseSetupState

@Composable
fun WorkoutExerciseFormScreen(
    state: WorkoutExerciseSetupState,
    onAction: (WorkoutExerciseSetupAction) -> Unit
) {
    Column (
        modifier = Modifier.padding(
            top = 20.dp,
            bottom = 20.dp,
            start = 40.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Configurar ejercicio",
            style = MaterialTheme.typography.titleMedium
        )

        Text (
            text = if (state.exerciseContext != null) { state.exerciseContext.name }
                    else "",
            style = MaterialTheme.typography.titleSmall
        )

        state.selectedWorkout?.let { workout ->
            Text(
                text = "Añadiendo a la rutina ${workout.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn(

        ) {
            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.weight,
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = {
                        onAction(WorkoutExerciseSetupAction.ChangeWeight(it))
                    },
                    errorMessage = state.weightError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.numSeries,
                    label = "Series",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        onAction(WorkoutExerciseSetupAction.ChangeNumSeries(it))
                    },
                    errorMessage = state.numSeriesError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.numReps,
                    label = "Repeticiones por serie",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        onAction(WorkoutExerciseSetupAction.ChangeNumReps(it))
                    },
                    errorMessage = state.numRepsError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.restSec,
                    label = "Descanso entre series (s)",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        onAction(WorkoutExerciseSetupAction.ChangeRestSec(it))
                    },
                    errorMessage = state.restSecError
                )
            }

            state.saveError?.let { message ->
                item { Text(message, color = MaterialTheme.colorScheme.error) }
            }

            item {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving && !state.isSaved,
                    onClick = { onAction(WorkoutExerciseSetupAction.SendForm) }
                ) {
                    Text(if (state.isSaving) "Guardando…" else "Confirmar")
                }
            }
        }
    }
}
