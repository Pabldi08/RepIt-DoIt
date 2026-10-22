package com.pad.repitdoit.features.workout.presentation.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.workout.presentation.WorkoutUIState
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutCompactLayout(
    uiState: WorkoutUIState,
    onExercisePreview: () -> Unit,
    onExit: () -> Unit
) {
    val workout = uiState.workout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = onExit
        ) {
            Text (
                text = "x"
            )
        }
        if (workout != null) {
            Text(
                text = workout.name,
            )
            Image(
                painter = painterResource(workout.image),
                contentDescription = null
            )
        }
    }
}