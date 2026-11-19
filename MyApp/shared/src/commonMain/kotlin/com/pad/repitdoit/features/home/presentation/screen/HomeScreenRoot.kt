package com.pad.repitdoit.features.home.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.di.HomeUseCasesAC
import com.pad.repitdoit.features.home.presentation.viewModel.HomeViewModel
import com.pad.repitdoit.features.home.presentation.layout.HomeLayout

@Composable
fun HomeScreenRoot(
    appContainer: HomeUseCasesAC,
    onWorkoutPreview: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel {
        HomeViewModel (
            appContainer = appContainer
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadDailyWorkouts()
        viewModel.loadProgress()
        viewModel.loadUsername()
    }

    HomeLayout(
        uiState = uiState,
        onWorkoutPreview = onWorkoutPreview
    )
}
