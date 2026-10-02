package com.pad.multiplatformapp.features.home.presentation

import androidx.compose.runtime.Composable
import com.example.myapp.AppWindowSize

@Composable
fun HomeScreen(
    windowSize: AppWindowSize
) {
    when(windowSize) {
        AppWindowSize.Compact -> HomeCompactLayout()
        AppWindowSize.Medium -> HomeMediumLayout()
        AppWindowSize.Extended -> HomeExtendedLayout()
    }
}