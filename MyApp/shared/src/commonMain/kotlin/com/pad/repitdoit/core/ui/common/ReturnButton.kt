package com.pad.repitdoit.core.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Ofrece una acción reutilizable para volver a la pantalla anterior.
@Composable
fun ReturnButton(
    onBack: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        TextButton(
            onClick = onBack
        ) {
            Text(
                text = "‹ Volver"
            )
        }
    }
}