package com.pad.repitdoit


import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.core.platform.supportsScanOptions
import com.pad.repitdoit.core.ui.theme.AppTheme
import com.pad.repitdoit.navigation.AppNavigation
import com.pad.repitdoit.navigation.MainScaffold
import com.pad.repitdoit.navigation.Route
import com.pad.repitdoit.navigation.ScannerScreen

enum class AppWindowSize {
    Compact,
    Medium,
    Extended
}

@Composable
fun App(
    appContainer: AppContainer,
    qrCameraScreen: @Composable (() -> Unit) -> Unit = { ScannerScreen() }
) {
    AppTheme {
        AppContent(appContainer, qrCameraScreen)
    }
}

@Composable
private fun AppContent(
    appContainer: AppContainer,
    qrCameraScreen: @Composable (() -> Unit) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    MainScaffold(
        showScanOptions = supportsScanOptions(),
        currentDestination = backStackEntry?.destination,
        onNavigate = { route -> navController.navigate(route)},
        onQrClick = {
            navController.navigate(Route.QrCamera) {
                launchSingleTop = true
            }
        }
    ) {
        AppNavigation(
            navController = navController,
            windowSize = windowSize,
            appContainer = appContainer,
            qrCameraScreen = qrCameraScreen
        )
    }
}

fun getWindowSize(
    windowSizeClass: WindowSizeClass
): AppWindowSize {
    return when {
        windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> AppWindowSize.Extended
        windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> AppWindowSize.Medium
        else -> AppWindowSize.Compact
    }
}




