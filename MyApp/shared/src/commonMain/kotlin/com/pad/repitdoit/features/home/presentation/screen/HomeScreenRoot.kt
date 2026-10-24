package com.pad.repitdoit.features.home.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pad.repitdoit.core.ui.adaptive.AppWindowSize
import com.pad.repitdoit.features.home.presentation.HomeViewModel

@Composable
fun HomeScreenRoot(
    windowSize: AppWindowSize,
    viewModel: HomeViewModel,
    onWorkoutPreview: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        windowSize = windowSize,
        uiState = uiState,
        onWorkoutPreview = onWorkoutPreview
    )
}