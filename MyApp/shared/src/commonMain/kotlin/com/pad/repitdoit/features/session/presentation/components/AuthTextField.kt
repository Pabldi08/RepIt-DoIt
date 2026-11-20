package com.pad.repitdoit.features.session.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Reutiliza el estilo de los campos de texto de acceso y registro.
@Composable
fun AuthTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true
    )
}