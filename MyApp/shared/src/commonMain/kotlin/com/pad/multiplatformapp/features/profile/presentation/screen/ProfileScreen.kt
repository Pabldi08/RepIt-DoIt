package com.pad.multiplatformapp.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.multiplatformapp.AppWindowSize
import com.pad.multiplatformapp.features.profile.presentation.layout.ProfileCompactLayout
import com.pad.multiplatformapp.features.profile.presentation.layout.ProfileMediumLayout
import com.pad.multiplatformapp.features.profile.presentation.layout.ProfileExtendedLayout

@Composable
fun ProfileScreen(windowSize: AppWindowSize) {
    when (windowSize) {
        AppWindowSize.Compact -> ProfileCompactLayout()
        AppWindowSize.Medium -> ProfileMediumLayout()
        AppWindowSize.Extended -> ProfileExtendedLayout()
    }
}
