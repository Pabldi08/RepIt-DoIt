package com.pad.multiplatformapp.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppName(
    modifier: Modifier = Modifier
) {
    Text (
        text = "RepIt-DoIt",
        style = MaterialTheme.typography.titleLargeEmphasized,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}