package com.pad.repitdoit.core.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NfcInfoPanel(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (visible) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 8.dp
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "Consultar máquina con NFC",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Text("Puedes escanear máquinas con NFC acercando el móvil a su etiqueta.")
                TextButton(onClick = onDismiss) {
                    Text("Entendido")
                }
            }
        }
    }
}
