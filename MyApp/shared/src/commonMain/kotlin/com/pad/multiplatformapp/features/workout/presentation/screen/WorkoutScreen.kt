package com.pad.multiplatformapp.features.workout.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.multiplatformapp.AppWindowSize
import com.pad.multiplatformapp.features.workout.presentation.WorkoutUIState
import com.pad.multiplatformapp.features.workout.presentation.layout.WorkoutCompactLayout
import com.pad.multiplatformapp.features.workout.presentation.layout.WorkoutExtendedLayout
import com.pad.multiplatformapp.features.workout.presentation.layout.WorkoutMediumLayout

@Composable
fun WorkoutScreen(
    windowSize: AppWindowSize,
    uiState: WorkoutUIState,
    onExercisePreview: () -> Unit,
    onExit: () -> Unit
) {
    when(windowSize) {
        AppWindowSize.Compact -> WorkoutCompactLayout(
            uiState = uiState,
            onExercisePreview = onExercisePreview,
            onExit = onExit
        )
        AppWindowSize.Medium -> WorkoutMediumLayout(
            uiState = uiState,
            onExercisePreview = onExercisePreview
        )
        AppWindowSize.Extended -> WorkoutExtendedLayout(
            uiState = uiState,
            onExercisePreview = onExercisePreview
        )
    }
}