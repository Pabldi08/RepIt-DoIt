package com.pad.repitdoit.core.ui.common

import androidx.compose.runtime.Composable

@Composable
expect fun OnBackHandler(enabled: Boolean, onBack: () -> Unit)