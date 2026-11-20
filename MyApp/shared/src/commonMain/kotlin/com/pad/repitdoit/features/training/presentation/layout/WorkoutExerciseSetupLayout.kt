package com.pad.repitdoit.features.training.presentation.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import com.pad.repitdoit.core.ui.common.ReturnButton
import com.pad.repitdoit.features.training.presentation.screen.ExerciseContextScreen
import com.pad.repitdoit.features.training.presentation.action.WorkoutExerciseSetupAction
import com.pad.repitdoit.features.training.presentation.screen.WorkoutExerciseFormScreen
import com.pad.repitdoit.features.training.presentation.screen.WorkoutSelectionScreen
import com.pad.repitdoit.features.training.presentation.state.WorkoutExerciseSetupState

// Organiza los pasos de selección y configuración de un ejercicio.
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WorkoutExerciseSetupLayout(
    state: WorkoutExerciseSetupState,
    onAction: (WorkoutExerciseSetupAction) -> Unit,
    onBack: () -> Unit
) {
    val navigator = rememberSupportingPaneScaffoldNavigator<Unit>()

    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,

        mainPane = {
            AnimatedPane {
                Column {
                ReturnButton(
                    onBack = onBack
                )

                val workout = state.selectedWorkout

                when {
                    state.isLoadingWorkouts -> {
                        CircularProgressIndicator()
                    }

                    workout != null -> {
                        WorkoutExerciseFormScreen(
                            state = state,
                            onAction = onAction
                        )
                    }

                    state.availableWorkouts.isNotEmpty() -> {
                        WorkoutSelectionScreen(
                            workouts = state.availableWorkouts,
                            onSelectWorkout = { workoutId ->
                                onAction(WorkoutExerciseSetupAction.SelectWorkout(workoutId))
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = state.workoutError
                                ?: "No tienes entrenamientos disponibles"
                        )
                    }
                }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                val exercise = state.exerciseContext

                when {
                    state.isLoadingExercise -> {
                        CircularProgressIndicator()
                    }

                    exercise != null -> {
                        ExerciseContextScreen(exerciseContext = exercise)
                    }

                    else -> {
                        Text(
                            text = state.exerciseError
                                ?: "Información del ejercicio no disponible en este momento"
                        )
                    }
                }
            }
        }
    )
}
