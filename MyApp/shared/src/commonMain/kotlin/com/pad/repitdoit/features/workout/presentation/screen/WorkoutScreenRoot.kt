package com.pad.repitdoit.features.workout.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pad.repitdoit.core.ui.adaptive.AppWindowSize
import com.pad.repitdoit.features.workout.presentation.WorkoutViewModel

@Composable
fun WorkoutScreenRoot(
    windowSize: AppWindowSize,
    viewModel: WorkoutViewModel,
    workoutId: String,
    onExercisePreview: () -> Unit,
    onExit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(workoutId, viewModel) {
        viewModel.loadWorkout(workoutId)
    }

    WorkoutScreen(
        windowSize = windowSize,
        uiState = uiState,
        onExercisePreview = onExercisePreview,
        onExit = onExit
    )
}