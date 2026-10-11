package com.pad.multiplatformapp.features.profile.presentation

import androidx.compose.runtime.Composable
import com.example.myapp.AppWindowSize

@Composable
fun ProfileScreen(windowSize: AppWindowSize) {
    when (windowSize) {
        AppWindowSize.Compact -> ProfileCompactLayout()
        AppWindowSize.Medium -> ProfileMediumLayout()
        AppWindowSize.Extended -> ProfileExtendedLayout()
    }
}
