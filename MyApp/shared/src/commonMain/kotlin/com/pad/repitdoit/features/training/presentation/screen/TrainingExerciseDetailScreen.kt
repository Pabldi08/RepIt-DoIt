package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.training.presentation.TrainingExercisePreview
import com.pad.repitdoit.features.training.presentation.TrainingSetPreview
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise

@Composable
fun TrainingExerciseDetailScreen(
    workout: Workout,
    exercise: WorkoutExercise,
    hardcodedExercise: TrainingExercisePreview,
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
                TrainingDetailHeader(
                    title = exercise.name,
                    //subtitle = "${TrainingPreviewData.plan.name} · ${workout.title}",
                    onBack = onBack
                )
            }
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
                Text(
                    text = "Series programadas\n(sigue hardcodeado",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                TrainingTableRow(
                    first = "Serie",
                    second ="Repeticiones",
                    third = "Peso",
                    heading = true
                )
            }
            itemsIndexed(hardcodedExercise.sets) {
                index,
                set ->
                    TrainingSetRow(
                        exerciseId = exercise.id,
                        index = index,
                        set = set
                    )
            }
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Historial en esta rutina\n(sigue hardcodeado)",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                TrainingTableRow(
                    first = "Fecha",
                    second = "Series × reps",
                    third ="Peso",
                    heading = true
                )
            }
            itemsIndexed(hardcodedExercise.history) {
                _, session ->
                    TrainingTableRow(
                        first = session.date,
                        second= session.sets,
                        third = session.weight
                    )
            }
            item {
                OutlinedCard(
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Evolución",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Próximamente",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Aquí irá la gráfica con la evolución de los pesos.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrainingSetRow(exerciseId: String, index: Int, set: TrainingSetPreview) {
    // Estado local de los campos. La persistencia se conectará con el ViewModel.
    var reps by remember(exerciseId, index, set) { mutableStateOf(set.reps.toString()) }
    var weight by remember(exerciseId, index, set) { mutableStateOf(set.weightKg.toString()) }
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${index + 1}", Modifier.weight(1.3f))
        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(0.8f)
        )
    }
}

@Composable
private fun TrainingTableRow(first: String, second: String, third: String, heading: Boolean = false) {
    Surface(shape = RoundedCornerShape(12.dp), color = if (heading) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val style = if (heading) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium
            Text(first, Modifier.weight(1.3f), style = style)
            Text(second, Modifier.weight(1f), style = style)
            Text(third, Modifier.weight(0.8f), style = style)
        }
    }
}
