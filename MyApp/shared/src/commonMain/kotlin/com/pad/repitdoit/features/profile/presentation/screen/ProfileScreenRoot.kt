package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.profile.presentation.ProfileViewModel

@Composable
fun ProfileScreenRoot(windowSize: AppWindowSize, appContainer: AppContainer) {
    val viewModel: ProfileViewModel = viewModel {
        ProfileViewModel(appContainer.getProfileUseCase)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(windowSize, uiState)
}
