package com.pad.multiplatformapp.features.workout.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pad.multiplatformapp.AppWindowSize
import com.pad.multiplatformapp.features.workout.presentation.WorkoutViewModel

@Composable
fun WorkoutScreenRoot(
    windowSize: AppWindowSize,
    viewModel: WorkoutViewModel,
    workoutId: Long,
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