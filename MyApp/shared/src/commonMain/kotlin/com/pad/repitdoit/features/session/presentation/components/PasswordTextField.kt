package com.pad.repitdoit.features.session.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

// Presenta el campo de contraseña de los formularios de autenticación.
@Composable
fun PasswordTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            TextButton(
                enabled = enabled,
                onClick = {
                    passwordVisible = !passwordVisible
                }
            ) {
                Text(
                    if (passwordVisible) "Ocultar"
                    else "Mostrar"
                )
            }
        }
    )
}