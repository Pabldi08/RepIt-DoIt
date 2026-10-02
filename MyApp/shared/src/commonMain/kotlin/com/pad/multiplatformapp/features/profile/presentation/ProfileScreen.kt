package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.runtime.Composable
import com.example.myapp.AppWindowSize
import com.pad.multiplatformapp.features.profile.presentation.ProfileCompactLayout

@Composable
fun ProfileScreen(
    windowShort: AppWindowSize
){
    when(windowShort){
        AppWindowSize.Compact -> ProfileCompactLayout()
        AppWindowSize.Medium -> TODO()
        AppWindowSize.Extended -> TODO()
    }
}
