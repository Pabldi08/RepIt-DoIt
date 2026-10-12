package com.pad.multiplatformapp.features.home.presentation

import androidx.compose.runtime.Composable
import com.pad.multiplatformapp.AppWindowSize

@Composable
fun HomeScreen(
    windowSize: AppWindowSize,
    onWorkoutPreview: () -> Unit
) {
    when(windowSize) {
        AppWindowSize.Compact -> HomeCompactLayout(onWorkoutPreview)
        AppWindowSize.Medium -> HomeCompactLayout(onWorkoutPreview)
        AppWindowSize.Extended -> HomeExtendedLayout()
    }
}