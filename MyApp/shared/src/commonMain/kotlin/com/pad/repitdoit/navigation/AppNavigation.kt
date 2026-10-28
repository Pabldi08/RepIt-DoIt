package com.pad.repitdoit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.ScannerScreen
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.home.presentation.screen.HomeScreenRoot
import com.pad.repitdoit.features.profile.presentation.ProfileScreen
import com.pad.repitdoit.features.profile.presentation.screen.ProfileScreen
import com.pad.repitdoit.features.training.presentation.layout.TrainingScreen
import com.pad.repitdoit.features.workout.presentation.screen.WorkoutScreenRoot

@Composable
fun AppNavigation(
    navController: NavHostController,
    windowSize: AppWindowSize,
    appContainer: AppContainer
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home
    ) {
        composable<Route.Home>{
            HomeScreenRoot(
                windowSize = windowSize,
                appContainer = appContainer,
                onWorkoutPreview = { workoutId ->
                    navController.navigate(Route.Workout(workoutId))
                }
            )
        }

        composable<Route.Training> {
            TrainingScreen(
                windowSize,
                appContainer
            )
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
                appContainer = appContainer,
                workoutId = route.workoutId,
                onExercisePreview = {},
                onExit = { navController.popBackStack() }
            )
        }

    }
}
