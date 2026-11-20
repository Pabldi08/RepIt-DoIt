package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.features.profile.presentation.ProfileUiState
import com.pad.repitdoit.features.profile.presentation.layout.ProfileLayout

// Presenta la pantalla principal del perfil.
@Composable
fun ProfileScreen(
    windowSize: AppWindowSize,
    uiState: ProfileUiState,
    onLogout: () -> Unit,
    onOpenSettings: () -> Unit
) {
    ProfileLayout(
        windowSize = windowSize,
        state = uiState,
        onLogout = onLogout,
        onOpenSettings = onOpenSettings
    )
}