package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.training.domain.model.Workout

// Muestra los entrenamientos disponibles para seleccionar.
@Composable
fun WorkoutSelectionScreen(
    workouts: List<Workout>,
    onSelectWorkout: (workoutId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Selecciona un entrenamiento",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Elige dónde quieres añadir el ejercicio. Después podrás configurar sus series, repeticiones y peso.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (workouts.isEmpty()) {
            item {
                Text(
                    text = "No tienes entrenamientos disponibles"
                )
            }
        }

        items(
            workouts,
            key = { it.id }
        ) { workout ->
            OutlinedCard(
                onClick = { onSelectWorkout(workout.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val count = workout.workoutExercises.size
                    Text(
                        text = if (count == 1) "1 ejercicio" else "$count ejercicios",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
