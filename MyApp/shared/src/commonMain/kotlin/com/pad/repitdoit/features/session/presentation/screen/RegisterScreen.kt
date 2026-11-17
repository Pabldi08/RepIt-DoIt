package com.pad.repitdoit.features.session.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.features.session.presentation.AuthAction
import com.pad.repitdoit.features.session.presentation.AuthUiState
import com.pad.repitdoit.features.session.presentation.layout.RegisterLayout

@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onAction: (AuthAction) -> Unit
) {
    RegisterLayout(
        state = uiState,
        onAction = onAction
    )
}