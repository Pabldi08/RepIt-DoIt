package com.pad.repitdoit.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pad.repitdoit.core.platform.supportsScanOptions
import com.pad.repitdoit.core.ui.adaptive.getWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.navigation.AppNavigation

@Composable
internal fun AppContent(
    appContainer: AppContainer
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    MainScaffold(
        showScanOptions = supportsScanOptions(),
        currentDestination = backStackEntry?.destination,
        onNavigate = { route -> navController.navigate(route)}
    ) {
        AppNavigation(
            navController = navController,
            windowSize = windowSize,
            appContainer = appContainer
        )
    }
}
