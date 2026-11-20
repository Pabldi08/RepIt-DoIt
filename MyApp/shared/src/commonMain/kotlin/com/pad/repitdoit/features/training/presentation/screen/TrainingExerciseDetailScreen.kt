package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.ReturnButton
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.presentation.action.TrainingAction
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseEditState
import com.pad.repitdoit.features.training.presentation.components.FormField

@Composable
fun TrainingExerciseDetailScreen(
    exercise: WorkoutExercise,
    editState: WorkoutExerciseEditState,
    onAction: (TrainingAction) -> Unit,
    onBack: () -> Unit
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 840.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReturnButton(
                    onBack = onBack
                )
            }

            item {
                Text(
                    exercise.name,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Ver si se queda o se va
            item {
                Surface(
                    shape = RoundedCornerShape(size = 20.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "${exercise.numSets} series × ${exercise.numReps} repeticiones",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "${exercise.weight} kg · Descanso de ${exercise.restSec} s entre series"
                        )
                    }
                }
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editState.weight ?: exercise.weight.toString(),
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { onAction(TrainingAction.ChangeWeight(exercise.id, it)) },
                    errorMessage = editState.weightError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editState.numSeries ?: exercise.numSets.toString(),
                    label = "Número de series",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { onAction(TrainingAction.ChangeNumSeries(exercise.id, it)) },
                    errorMessage = editState.numSeriesError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editState.numReps ?: exercise.numReps.toString(),
                    label = "Repeticiones",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { onAction(TrainingAction.ChangeNumReps(exercise.id, it)) },
                    errorMessage = editState.numRepsError
                )
            }

            item {
                FormField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editState.restSec ?: exercise.restSec.toString(),
                    label = "Descanso (segundos)",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { onAction(TrainingAction.ChangeRestSec(exercise.id, it)) },
                    errorMessage = editState.restSecError
                )
            }

            editState.saveError?.let { message ->
                item { Text(message, color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}
