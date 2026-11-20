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

// Organiza el formulario de registro y sus acciones.
@Composable
internal fun RegisterLayout(
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

        // Aprovecha el espacio horizontal para mostrar la marca junto al formulario.
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
                    AuthHeader(
                        subtitle = "Empieza a entrenar",
                        isWide = true
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Crear cuenta",
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

                        AuthTextField(
                            value = state.displayName,
                            label = "Nombre visible",
                            enabled = !state.isLoading,
                            onValueChange = {
                                onAction(AuthAction.DisplayNameChanged(it))
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

                        PasswordTextField(
                            value = state.repeatPassword,
                            label = "Repetir contraseña",
                            enabled = !state.isLoading,
                            onValueChange = {
                                onAction(AuthAction.RepeatPasswordChanged(it))
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
                                onAction(AuthAction.Register)
                            }
                        ) {
                            Text(
                                if (state.isLoading) "Creando cuenta..."
                                else "Crear cuenta"
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "¿Ya tienes cuenta?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(
                            enabled = !state.isLoading,
                            onClick = {
                                onAction(AuthAction.ShowLogin)
                            }
                        ) {
                            Text("Iniciar sesión")
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 440.dp)
                        .fillMaxWidth()
                        .imePadding() // Añadir aquí
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AuthHeader(isWide = false)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Crear cuenta",
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

                    AuthTextField(
                        value = state.displayName,
                        label = "Nombre visible",
                        enabled = !state.isLoading,
                        onValueChange = {
                            onAction(AuthAction.DisplayNameChanged(it))
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

                    PasswordTextField(
                        value = state.repeatPassword,
                        label = "Repetir contraseña",
                        enabled = !state.isLoading,
                        onValueChange = {
                            onAction(AuthAction.RepeatPasswordChanged(it))
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
                            onAction(AuthAction.Register)
                        }
                    ) {
                        Text(
                            if (state.isLoading) "Creando cuenta..."
                            else "Crear cuenta"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Ya tienes cuenta?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        enabled = !state.isLoading,
                        onClick = {
                            onAction(AuthAction.ShowLogin)
                        }
                    ) {
                        Text("Iniciar sesión")
                    }
                }
            }
        }
    }
}