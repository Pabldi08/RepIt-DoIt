package com.pad.repitdoit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.catalogue.presentation.screen.CatalogueScreenRoot
import com.pad.repitdoit.features.gamification.presentation.screen.GamificationScreenRoot
import com.pad.repitdoit.features.home.presentation.screen.HomeScreenRoot
import com.pad.repitdoit.features.profile.presentation.screen.ProfileScreenRoot
import com.pad.repitdoit.features.training.presentation.screen.TrainingScreenRoot
import com.pad.repitdoit.features.training.presentation.screen.WorkoutExerciseSetupScreenRoot
import com.pad.repitdoit.features.training.presentation.state.rememberWorkoutSessionState
import com.pad.repitdoit.features.training.presentation.screen.WorkoutSessionScreenRoot
@Composable
fun AppNavigation(
    navController: NavHostController,
    windowSize: AppWindowSize,
    appContainer: AppContainer,
    qrCameraScreen: @Composable ((String) -> Unit) -> Unit,
    activeWorkoutId: String?,
    onStartWorkout: (String) -> Unit,
    onCloseWorkout: () -> Unit
) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val activeSession = if (activeWorkoutId != null) {
        key(activeWorkoutId) { rememberWorkoutSessionState(activeWorkoutId, appContainer) }
    } else {
        null
    }

    val session by rememberUpdatedState(activeSession)
    val startWorkout by rememberUpdatedState(onStartWorkout)
    val closeWorkout by rememberUpdatedState(onCloseWorkout)

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

        composable<Route.Training> { entry ->
            val workoutSession = session
            if (workoutSession != null) {
                WorkoutSessionScreenRoot(
                    session = workoutSession,
                    isCurrent = currentEntry == entry,
                    onExit = closeWorkout
                )
            } else {
                TrainingScreenRoot(
                    appContainer = appContainer,
                    onStartWorkout = startWorkout,
                    onAddExercise = { workoutId ->
                        navController.navigate(
                            Route.Catalogue(workoutId = workoutId)
                        )
                    }
                )
            }
        }

        composable<Route.Gamification> {
            GamificationScreenRoot(appContainer)
        }

        composable<Route.QrCamera> {
            qrCameraScreen { equipmentId ->
                navController.navigate(Route.Catalogue(machineId = equipmentId)) {
                    popUpTo<Route.QrCamera> { inclusive = true }
                }
            }
        }

        composable<Route.Profile> {
            ProfileScreenRoot(windowSize, appContainer)
        }

        composable<Route.Workout> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.Workout>()
            TrainingScreenRoot(
                appContainer = appContainer,
                initialWorkoutId = route.workoutId,
                onStartWorkout = startWorkout,
                onAddExercise = { workoutId ->
                    navController.navigate(
                        Route.Catalogue(workoutId = workoutId)
                    )
                }
            )
        }

        composable<Route.Catalogue> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.Catalogue>()

            CatalogueScreenRoot(
                appContainer = appContainer,
                machineId = route.machineId,
                onBack = { navController.popBackStack() },
                onAddExercise = { exerciseId ->
                    val origin = navController.previousBackStackEntry
                    navController.navigate(
                        Route.WorkoutExerciseSetup(
                            exerciseId = exerciseId,
                            workoutId = route.workoutId,
                            returnToTraining = origin?.destination?.hasRoute<Route.Training>() == true,
                            returnWorkoutId = if (origin?.destination?.hasRoute<Route.Workout>() == true) {
                                origin.toRoute<Route.Workout>().workoutId
                            } else null
                        )
                    )
                }
            )
        }

        composable<Route.WorkoutExerciseSetup> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.WorkoutExerciseSetup>()
            WorkoutExerciseSetupScreenRoot(
                appContainer = appContainer,
                exerciseId = route.exerciseId,
                workoutId = route.workoutId,
                onBack = { navController.popBackStack() },
                onSaved = { savedWorkoutId ->
                    val returnedToOrigin = when {
                        route.returnToTraining ->
                            navController.popBackStack<Route.Training>(inclusive = false)
                        route.returnWorkoutId == savedWorkoutId ->
                            navController.popBackStack(
                                Route.Workout(savedWorkoutId), inclusive = false
                            )
                        else -> false
                    }
                    if (!returnedToOrigin) {
                        navController.navigate(Route.Workout(savedWorkoutId)) {
                            popUpTo<Route.Catalogue> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
