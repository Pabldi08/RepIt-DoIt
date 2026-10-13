package com.pad.multiplatformapp.features.home.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.multiplatformapp.AppWindowSize
import com.pad.multiplatformapp.features.home.presentation.HomeUIState
import com.pad.multiplatformapp.features.home.presentation.layout.HomeCompactLayout
import com.pad.multiplatformapp.features.home.presentation.layout.HomeExtendedLayout

@Composable
fun HomeScreen(
    windowSize: AppWindowSize,
    uiState: HomeUIState,
    onWorkoutPreview: () -> Unit
) {
    when(windowSize) {
        AppWindowSize.Compact -> HomeCompactLayout(
            uiState = uiState,
            onWorkoutPreview = onWorkoutPreview
        )
        AppWindowSize.Medium -> HomeCompactLayout(
            uiState = uiState,
            onWorkoutPreview = onWorkoutPreview
        )
        AppWindowSize.Extended -> HomeExtendedLayout(
            uiState = uiState,
            onWorkoutPreview = onWorkoutPreview
        )
    }
}