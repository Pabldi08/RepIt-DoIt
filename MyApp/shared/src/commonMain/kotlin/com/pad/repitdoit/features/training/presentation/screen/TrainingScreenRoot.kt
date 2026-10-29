package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.training.presentation.TrainingViewModel
import com.pad.repitdoit.features.training.presentation.layout.TrainingLayout

@Composable
fun TrainingScreenRoot(
    windowSize: AppWindowSize,
    appContainer: AppContainer,
    initialWorkoutId: String? = null
) {
    val viewModel: TrainingViewModel = viewModel {
        TrainingViewModel(
            createWorkoutUseCase = appContainer.createWorkoutUseCase,
            getUsersWorkoutsUseCase = appContainer.getUsersWorkoutsUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainingLayout(
        windowSize = windowSize,
        state = uiState,
        initialWorkoutId = initialWorkoutId,
        onCreateWorkout = viewModel::createWorkout
    )
}