package com.pad.multiplatformapp.ui.common

import androidx.compose.runtime.Composable

@Composable
expect fun OnBackHandler(enabled: Boolean, onBack: () -> Unit)
