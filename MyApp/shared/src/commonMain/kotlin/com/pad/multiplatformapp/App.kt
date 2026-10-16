package com.pad.multiplatformapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.pad.multiplatformapp.di.DefaultAppContainer
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.*
import com.pad.multiplatformapp.features.home.presentation.screen.HomeScreenRoot
import com.pad.multiplatformapp.features.training.presentation.TrainingScreen
import com.pad.multiplatformapp.features.profile.presentation.ProfileScreen
import com.pad.multiplatformapp.features.workout.presentation.screen.WorkoutScreenRoot
import com.pad.multiplatformapp.navigation.Route
import com.pad.multiplatformapp.ui.theme.AppTheme


enum class AppWindowSize {
    Compact,
    Medium,
    Extended
}

@Composable
@Preview
fun App() {
    AppTheme {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val appContainer = remember {DefaultAppContainer()}
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    MainScaffold(
        currentDestination = backStackEntry?.destination,
        onNavigate = { route -> navController.navigate(route)}
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Home
        ) {
            composable<Route.Home>{
                HomeScreenRoot(
                    windowSize = windowSize,
                    viewModel = appContainer.homeViewModel,
                    onWorkoutPreview = { navController.navigate(Route.Workout(1)) }
                )
            }

            composable<Route.Training> {
                TrainingScreen(windowSize)
            }

            composable<Route.Scanner> {
                ScannerScreen()
            }

            composable<Route.Profile> {
                ProfileScreen(windowSize)
            }

            composable<Route.Workout>{ backStackEntry ->
                val route = backStackEntry.toRoute<Route.Workout>()
                WorkoutScreenRoot(
                    windowSize = windowSize,
                    viewModel = appContainer.workoutViewModel,
                    workoutId = route.workoutId,
                    onExercisePreview = {},
                    onExit = { navController.popBackStack() }
                )
            }


        }
    }
}

@Composable
fun MainScaffold (
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit,
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        navigationSuiteItems = {
            AppNavigationItems(
                currentDestination = currentDestination,
                onNavigate = onNavigate
            )
        }
    ) {
        content()
    }
}

fun NavigationSuiteScope.AppNavigationItems(
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit
) {
    item (
        selected = currentDestination?.hasRoute<Route.Home>() == true,
        onClick = { onNavigate(Route.Home) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconHouse),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Home") }
    )
    item (
        selected = currentDestination?.hasRoute<Route.Training>() == true,
        onClick = { onNavigate(Route.Training) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconDumbbell),
                contentDescription = "Training",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Training") }
    )
    item (
        selected = currentDestination?.hasRoute<Route.Scanner>() == true,
        onClick = { onNavigate(Route.Scanner) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconScanner),
                contentDescription = "Scanner",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Scanner") }
    )
    item (
        selected = currentDestination?.hasRoute<Route.Profile>() == true,
        onClick = { onNavigate(Route.Profile) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconProfile),
                contentDescription = "Profile",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Profile") }
    )
}

@Composable
fun ScannerScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.placeholder),
                null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( "Scanner screen" )
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



