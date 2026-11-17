package com.pad.repitdoit


import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination.Companion.hasRoute
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
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.pad.repitdoit.features.session.presentation.screen.AuthRoot

enum class AppWindowSize {
    Compact,
    Medium,
    Extended
}

@Composable
fun App(
    appContainer: AppContainer,
    qrCameraScreen: @Composable () -> Unit = {
        ScannerScreen()
    }
) {

    AppTheme {

        val session by appContainer
            .getCurrentSessionUseCase()
            .collectAsStateWithLifecycle()

        key(session?.id) {

            val screenOwner = remember {
                object : ViewModelStoreOwner {
                    override val viewModelStore = ViewModelStore()
                }
            }

            DisposableEffect(screenOwner) {
                onDispose {
                    screenOwner.viewModelStore.clear()
                }
            }

            CompositionLocalProvider(
                LocalViewModelStoreOwner provides screenOwner
            ) {

                if (session == null) {
                    AuthRoot(appContainer)
                } else {
                    AppContent(
                        appContainer,
                        qrCameraScreen
                    )
                }
            }
        }
    }
}

@Composable
private fun AppContent(
    appContainer: AppContainer,
    qrCameraScreen: @Composable () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    var activeWorkoutId by rememberSaveable { mutableStateOf<String?>(null) }

    MainScaffold(
        showScanOptions = supportsScanOptions(),
        currentDestination = backStackEntry?.destination,
        showingWorkoutSession = activeWorkoutId != null &&
            backStackEntry?.destination?.hasRoute<Route.Training>() == true,
        onNavigate = { route ->
            if (route != Route.Training || navController.currentDestination?.hasRoute<Route.Training>() != true) {
                navController.navigate(route) { launchSingleTop = true }
            }
        },
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
            qrCameraScreen = qrCameraScreen,
            activeWorkoutId = activeWorkoutId,
            onStartWorkout = { workoutId ->
                if (activeWorkoutId == null) activeWorkoutId = workoutId
                if (navController.currentDestination?.hasRoute<Route.Training>() != true) {
                    navController.navigate(Route.Training) { launchSingleTop = true }
                }
            },
            onCloseWorkout = { activeWorkoutId = null }
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




