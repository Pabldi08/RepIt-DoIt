package com.pad.repitdoit.features.profile.presentation

import androidx.compose.runtime.Composable
import com.pad.repitdoit.AppWindowSize

@Composable
fun ProfileScreen(windowSize: AppWindowSize) {
    when (windowSize) {
        AppWindowSize.Compact -> ProfileCompactLayout()
        AppWindowSize.Medium -> ProfileMediumLayout()
        AppWindowSize.Extended -> ProfileExtendedLayout()
    }
}
