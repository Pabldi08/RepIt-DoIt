package com.pad.repitdoit.features.session.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.AppLogo
import com.pad.repitdoit.core.ui.common.AppName

// Presenta la marca en las pantallas de autenticación de escritorio.
@Composable
internal fun AuthDesktopBrand(
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppLogo(
            modifier = Modifier.size(220.dp)
        )

        AppName()

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}