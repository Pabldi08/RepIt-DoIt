package com.pad.repitdoit.core.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

// Muestra el nombre de la aplicación con su estilo de marca.
@Composable
fun AppName(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text (
        text = "RepIt-DoIt",
        style = MaterialTheme.typography.titleLargeEmphasized,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        fontSize = fontSize
    )
}