package com.pad.repitdoit.features.session.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.session.presentation.AuthAction
import com.pad.repitdoit.features.session.presentation.AuthMode
import com.pad.repitdoit.features.session.presentation.AuthViewModel

// Selecciona el flujo de acceso o registro según el estado de autenticación.
@Composable
fun AuthRoot(appContainer: AppContainer) {

    val viewModel: AuthViewModel = viewModel {
        AuthViewModel(
            createUserUseCase = appContainer.createUserUseCase,
            loginUseCase = appContainer.loginUseCase
        )
    }

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    OnBackHandler(
        enabled = uiState.mode == AuthMode.REGISTER &&
                !uiState.isLoading
    ) {
        viewModel.onAction(AuthAction.ShowLogin)
    }

    when (uiState.mode) {

        AuthMode.LOGIN -> {
            LoginScreen(
                uiState = uiState,
                onAction = viewModel::onAction
            )
        }

        AuthMode.REGISTER -> {
            RegisterScreen(
                uiState = uiState,
                onAction = viewModel::onAction
            )
        }
    }
}