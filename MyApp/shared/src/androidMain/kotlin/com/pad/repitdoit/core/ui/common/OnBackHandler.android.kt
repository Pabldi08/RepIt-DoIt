package com.pad.repitdoit.core.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

// Conecta la acción de volver con el mecanismo de navegación de la plataforma.
@Composable
actual fun OnBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
