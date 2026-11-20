package com.pad.repitdoit.features.training.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType


// Reutiliza la presentación de un campo del formulario de entrenamiento.
@Composable
fun FormField(
    value: String,
    label: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        enabled = enabled,
        isError = errorMessage != null,
        supportingText = errorMessage?.let { message ->
            {
                Text(
                    text = message
                )
            }
        }
    )
}