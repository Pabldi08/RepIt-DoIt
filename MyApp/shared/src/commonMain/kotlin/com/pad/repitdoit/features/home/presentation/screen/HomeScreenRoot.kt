package com.pad.repitdoit.features.home.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.home.presentation.HomeViewModel

@Composable
fun HomeScreenRoot(
    windowSize: AppWindowSize,
    appContainer: AppContainer,
    onWorkoutPreview: (String) -> Unit
) {
    val viewModel = HomeViewModel(
        getHomeDailyWorkoutsUseCase = appContainer.getHomeDailyWorkoutsUseCase
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        windowSize = windowSize,
        uiState = uiState,
        onWorkoutPreview = onWorkoutPreview
    )
}