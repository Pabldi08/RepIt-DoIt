package com.pad.repitdoit.features.training.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.core.ui.common.Header
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.home.presentation.components.DailyWorkoutCard
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_muscles
import repitdoit.shared.generated.resources.training_legs

@Composable
fun TrainingScreen(
    windowSize: AppWindowSize,
    appContainer: AppContainer,
) {
    val viewModel: TrainingViewModel = viewModel {
        TrainingViewModel(
            createWorkoutUseCase = appContainer.createWorkoutUseCase,
            getUsersWorkoutsUseCase = appContainer.getUsersWorkoutsUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (windowSize) {
        AppWindowSize.Compact, AppWindowSize.Medium -> TrainingCompactLayout(
            uiState,
            onAddWorkout = viewModel::createWorkout,
            errorMessage = uiState.errorMessage
        )
        AppWindowSize.Extended -> TrainingExtendedLayout()
    }
}

@Composable
fun TrainingCompactLayout(
    uiState: TrainingUIState,
    onAddWorkout: (String) -> Unit,
    errorMessage: String? = null
) {
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var workoutName by rememberSaveable { mutableStateOf("") }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Añadir entrenamiento") },
            text = {
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = { workoutName = it },
                    label = { Text("Nombre del entrenamiento") },
                    singleLine = true
                )
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancelar")
                }
            },
            confirmButton = {
                TextButton(
                    enabled = workoutName.isNotBlank(),
                    onClick = {
                        onAddWorkout(workoutName.trim())
                        showCreateDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp)
        )
        Text(
            text = "Mis entrenamientos",
            style = MaterialTheme.typography.titleMediumEmphasized,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items (
                items = uiState.workouts,
                key = { it.id }
            ) { workout ->
                TrainingCard(
                    title = workout.name,
                    xp = 200,
                    day = "HOY",
                    image = workout.image,
                    exercises = workout.workoutExercises,
                    duration = workout.estimatedTime
                )
            }
//            item {
//                TrainingCard(
//                    title = "Tren Superior",
//                    xp = 200,
//                    day = "HOY",
//                    image = Res.drawable.training_muscles,
//                    exercises = listOf(
//                        ExerciseItem("Press de banca", "3x10 repeticiones · 40 kg"),
//                        ExerciseItem("Remo con mancuerna", "3x12 repeticiones · 16 kg"),
//                        ExerciseItem(name = "Jalón al pecho", "4x10 repeticiones · 50 kg"),
//                        ExerciseItem("Curl de bíceps", "4x10 repeticiones · 20 kg")
//                    )
//                )
//            }
//            item {
//                TrainingCard(
//                    title = "Pierna",
//                    xp = 150,
//                    day = "MAÑANA",
//                    image = Res.drawable.training_legs,
//                    exercises = listOf(
//                        ExerciseItem("Press de piernas", "3x10 repeticiones · 150 kg"),
//                        ExerciseItem("Abductores", "3x12 repeticiones · 40 kg"),
//                        ExerciseItem("Extensión de cuádriceps", "4x10 repeticiones · 80 kg")
//                    )
//                )
//            }
            item {
                OutlinedCard(
                    onClick = {
                        workoutName = ""
                        showCreateDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Añadir entrenamiento",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrainingExtendedLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp)
        )

        Text(
            text = "Mis entrenamientos",
            style = MaterialTheme.typography.titleMediumEmphasized,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            item {
//                TrainingCard(
//                    title = "Tren Superior",
//                    xp = 200,
//                    day = "HOY",
//                    image = Res.drawable.training_muscles,
//                    exercises = listOf(
//                        ExerciseItem("Press de banca", "3x10 repeticiones · 40 kg"),
//                        ExerciseItem("Remo con mancuerna", "3x12 repeticiones · 16 kg"),
//                        ExerciseItem(name = "Jalón al pecho", "4x10 repeticiones · 50 kg"),
//                        ExerciseItem("Curl de bíceps", "4x10 repeticiones · 20 kg")
//                    )
//                )
//            }
//            item {
//                TrainingCard(
//                    title = "Pierna",
//                    xp = 150,
//                    day = "MAÑANA",
//                    image = Res.drawable.training_legs,
//                    exercises = listOf(
//                        ExerciseItem("Press de piernas", "3x10 repeticiones · 150 kg"),
//                        ExerciseItem("Abductores", "3x12 repeticiones · 40 kg"),
//                        ExerciseItem("Extensión de cuádriceps", "4x10 repeticiones · 80 kg")
//                    )
//                )
//            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedCard(
                    onClick = { /* Aquí irá la acción de añadir */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Añadir entrenamiento",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
