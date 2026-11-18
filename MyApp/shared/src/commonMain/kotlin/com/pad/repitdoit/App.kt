package com.pad.repitdoit


import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

enum class AppWindowSize {
    Compact,
    Medium,
    Extended
}

@Composable
fun App(
    appContainer: AppContainer,
    qrCameraScreen: @Composable ((String) -> Unit) -> Unit = { _ -> ScannerScreen() },
    nfcEquipmentIds: Flow<String> = emptyFlow(),
    toScreen: Route? = null
) {
    AppTheme {
        AppContent(appContainer, qrCameraScreen, nfcEquipmentIds, toScreen)
    }
}

@Composable
private fun AppContent(
    appContainer: AppContainer,
    qrCameraScreen: @Composable ((String) -> Unit) -> Unit,
    nfcEquipmentIds: Flow<String>,
    toScreen: Route?
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    var nfcPanelVisible by remember { mutableStateOf(false) }
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
        nfcPanelVisible = nfcPanelVisible,
        onNfcPanelVisibilityChange = { nfcPanelVisible = it },
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
    LaunchedEffect(toScreen) {
        val route = toScreen ?: return@LaunchedEffect
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
    LaunchedEffect(nfcEquipmentIds, navController) {
        nfcEquipmentIds.collect { equipmentId ->
            nfcPanelVisible = false

            val currentEntry = navController.currentBackStackEntry
            val currentMachineId = currentEntry
                ?.takeIf { it.destination.hasRoute<Route.Catalogue>() }
                ?.toRoute<Route.Catalogue>()
                ?.machineId

            if (currentMachineId == equipmentId) return@collect

            navController.navigate(Route.Catalogue(machineId = equipmentId)) {
                when {
                    currentMachineId != null ->
                        popUpTo<Route.Catalogue> { inclusive = true }
                    currentEntry?.destination?.hasRoute<Route.QrCamera>() == true ->
                        popUpTo<Route.QrCamera> { inclusive = true }
                }
            }
        }
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




