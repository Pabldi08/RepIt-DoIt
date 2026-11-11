package com.pad.repitdoit.features.catalogue.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.catalogue.presentation.CatalogueAction
import com.pad.repitdoit.features.catalogue.presentation.CatalogueState

@Composable
fun ExerciseSearchScreen(
    state: CatalogueState,
    selectedExerciseId: String?,
    onAction: (CatalogueAction) -> Unit,
    onExerciseDetails: (exerciseId: String) -> Unit,
    onBack: () -> Unit
) {
    CatalogueSearchContent(
        title = "Buscar ejercicio",
        state = state,
        selectedExerciseId = selectedExerciseId,
        onAction = onAction,
        onExerciseDetails = onExerciseDetails,
        onBack = onBack
    )
}

@Composable
internal fun CatalogueSearchContent(
    title: String,
    state: CatalogueState,
    selectedExerciseId: String?,
    onAction: (CatalogueAction) -> Unit,
    onExerciseDetails: (String) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { TextButton(onClick = onBack) { Text("Volver") } }
        item { Text(title, style = MaterialTheme.typography.headlineMedium) }
        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = { onAction(CatalogueAction.ChangeQuery(it)) },
                label = { Text("Nombre del ejercicio") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        state.errorMessage?.let { message ->
            item { Text(message, color = MaterialTheme.colorScheme.error) }
        }
        items(state.filteredExercises, key = { it.id }) { exercise ->
            OutlinedCard(
                onClick = { onExerciseDetails(exercise.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = if (exercise.id == selectedExerciseId) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(exercise.name, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
