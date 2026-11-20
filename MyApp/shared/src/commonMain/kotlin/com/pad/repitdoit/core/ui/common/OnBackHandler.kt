package com.pad.repitdoit.core.ui.common

import androidx.compose.runtime.Composable

// Conecta la acción de volver con el mecanismo de navegación de la plataforma.
@Composable
expect fun OnBackHandler(enabled: Boolean, onBack: () -> Unit)