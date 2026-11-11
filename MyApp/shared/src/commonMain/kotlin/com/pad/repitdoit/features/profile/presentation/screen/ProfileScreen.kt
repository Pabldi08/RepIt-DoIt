package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.features.profile.presentation.ProfileUiState
import com.pad.repitdoit.features.profile.presentation.layout.ProfileLayout

@Composable
fun ProfileScreen(windowSize: AppWindowSize, uiState: ProfileUiState) {
    ProfileLayout(windowSize, uiState)
}
