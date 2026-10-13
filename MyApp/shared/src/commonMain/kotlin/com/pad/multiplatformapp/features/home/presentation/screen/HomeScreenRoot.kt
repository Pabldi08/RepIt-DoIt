package com.pad.multiplatformapp.features.home.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pad.multiplatformapp.AppWindowSize
import com.pad.multiplatformapp.features.home.presentation.HomeViewModel

@Composable
fun HomeScreenRoot(
    windowSize: AppWindowSize,
    viewModel: HomeViewModel,
    onWorkoutPreview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        windowSize = windowSize,
        uiState = uiState,
        onWorkoutPreview = onWorkoutPreview
    )
}