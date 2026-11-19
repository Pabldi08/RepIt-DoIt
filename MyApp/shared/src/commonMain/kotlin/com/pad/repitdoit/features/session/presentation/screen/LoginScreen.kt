package com.pad.repitdoit.features.session.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.features.session.presentation.AuthAction
import com.pad.repitdoit.features.session.presentation.AuthUiState
import com.pad.repitdoit.features.session.presentation.layout.LoginLayout

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onAction: (AuthAction) -> Unit
) {
    LoginLayout(
        state = uiState,
        onAction = onAction
    )
}