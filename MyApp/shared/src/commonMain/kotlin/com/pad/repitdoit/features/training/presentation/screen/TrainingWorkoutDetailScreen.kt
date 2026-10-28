package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.training.presentation.TrainingPreviewData
import com.pad.repitdoit.features.training.presentation.TrainingWorkoutPreview
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.icon_details
import repitdoit.shared.generated.resources.icon_play

@Composable
internal fun TrainingDetailHeader(title: String, subtitle: String, onBack: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(onClick = onBack) { Text("‹ Volver") }
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun TrainingWorkoutDetailScreen(
    workout: TrainingWorkoutPreview,
    onExercise: (String) -> Unit,
    onBack: () -> Unit,
    onStart: () -> Unit = {},
    onDeleteExercise: (String) -> Unit = {},
    onAddExercise: () -> Unit = {}
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 960.dp).fillMaxSize(),
            contentPadding = PaddingValues(20.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrainingDetailHeader(workout.title,
                    "${TrainingPreviewData.plan.name} · ${workout.day}", onBack)
            }
            item {
                Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("${workout.exercises.size} ejercicios · ${workout.minutes} min · +${workout.xp} XP", style = MaterialTheme.typography.titleMedium)
                        Button(
                            onClick = onStart,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(painterResource(Res.drawable.icon_play), contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Iniciar entrenamiento")
                        }
                    }
                }
            }
            item {
                Text("Ejercicios", style = MaterialTheme.typography.titleLarge)
                Text("Toca un ejercicio para ver sus series y su historial.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            itemsIndexed(workout.exercises, key = { _, exercise -> exercise.id }) { index, exercise ->
                OutlinedCard(onClick = { onExercise(exercise.id) }, shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("EJERCICIO ${index + 1}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                        Text("${exercise.sets.size} series · Descanso ${exercise.rest}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${exercise.sets.first().reps} repeticiones · ${exercise.sets.first().weightKg} kg por serie")
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onExercise(exercise.id) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(painterResource(Res.drawable.icon_details), contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Detalles")
                            }
                            OutlinedButton(
                                onClick = { onDeleteExercise(exercise.id) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
            item {
                OutlinedButton(onClick = onAddExercise, modifier = Modifier.fillMaxWidth()) {
                    Text("+ Añadir ejercicio")
                }
            }
        }
    }
}
