package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.training.domain.model.ExerciseContext

@Composable
fun ExerciseContextScreen(exerciseContext: ExerciseContext) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Información del ejercicio",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Text(
                text = exerciseContext.name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            Text(
                text = exerciseContext.description?.takeIf { it.isNotBlank() }
                    ?: "Sin descripción disponible"
            )
        }
    }
}

