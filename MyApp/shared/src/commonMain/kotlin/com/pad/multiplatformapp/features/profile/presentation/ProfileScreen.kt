package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.myapp.AppWindowSize
import com.pad.multiplatformapp.features.profile.presentation.ProfileCompactLayout

@Composable
fun ProfileScreen(windowSize: AppWindowSize) {
    val maxContentWidth = when (windowSize) {
        AppWindowSize.Compact -> 480.dp
        AppWindowSize.Medium, AppWindowSize.Extended -> 520.dp
    }
    ProfileCompactLayout(maxContentWidth = maxContentWidth)
}
