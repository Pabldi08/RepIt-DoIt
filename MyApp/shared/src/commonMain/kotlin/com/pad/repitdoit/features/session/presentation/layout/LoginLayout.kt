package com.pad.repitdoit.features.session.presentation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.session.presentation.AuthAction
import com.pad.repitdoit.features.session.presentation.AuthUiState
import com.pad.repitdoit.features.session.presentation.components.AuthHeader
import com.pad.repitdoit.features.session.presentation.components.AuthTextField
import com.pad.repitdoit.features.session.presentation.components.PasswordTextField

@Composable
internal fun LoginLayout(
    state: AuthUiState,
    onAction: (AuthAction) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .displayCutoutPadding()
    ) {
        val isWideScreen = maxWidth >= 900.dp

        if (isWideScreen) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthDesktopBrand(
                        subtitle = "Bienvenido de nuevo"
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 420.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        AuthTextField(
                            value = state.username,
                            label = "Nombre de usuario",
                            enabled = !state.isLoading,
                            onValueChange = {
                                onAction(AuthAction.UsernameChanged(it))
                            }
                        )

                        PasswordTextField(
                            value = state.password,
                            label = "Contraseña",
                            enabled = !state.isLoading,
                            onValueChange = {
                                onAction(AuthAction.PasswordChanged(it))
                            }
                        )

                        state.error?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 50.dp),
                            enabled = !state.isLoading,
                            onClick = {
                                onAction(AuthAction.Login)
                            }
                        ) {
                            Text(
                                if (state.isLoading) "Iniciando sesión..."
                                else "Iniciar sesión"
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "¿No tienes cuenta?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(
                            enabled = !state.isLoading,
                            onClick = {
                                onAction(AuthAction.ShowRegister)
                            }
                        ) {
                            Text("Regístrate")
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 440.dp)
                        .fillMaxWidth()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AuthHeader(
                        subtitle = "Bienvenido de nuevo"
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    AuthTextField(
                        value = state.username,
                        label = "Nombre de usuario",
                        enabled = !state.isLoading,
                        onValueChange = {
                            onAction(AuthAction.UsernameChanged(it))
                        }
                    )

                    PasswordTextField(
                        value = state.password,
                        label = "Contraseña",
                        enabled = !state.isLoading,
                        onValueChange = {
                            onAction(AuthAction.PasswordChanged(it))
                        }
                    )

                    state.error?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        enabled = !state.isLoading,
                        onClick = {
                            onAction(AuthAction.Login)
                        }
                    ) {
                        Text(
                            if (state.isLoading) "Iniciando sesión..."
                            else "Iniciar sesión"
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                TextButton(
                    enabled = !state.isLoading,
                    onClick = {
                        onAction(AuthAction.ShowRegister)
                    }
                ) {
                    Text("Regístrate")
                }
            }
        }
    }
}