package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.features.profile.presentation.layout.ProfileCompactLayout
import com.pad.repitdoit.features.profile.presentation.layout.ProfileMediumLayout
import com.pad.repitdoit.features.profile.presentation.layout.ProfileExtendedLayout

@Composable
fun ProfileScreen(windowSize: AppWindowSize) {
    when (windowSize) {
        AppWindowSize.Compact -> ProfileCompactLayout()
        AppWindowSize.Medium -> ProfileMediumLayout()
        AppWindowSize.Extended -> ProfileExtendedLayout()
    }
}
