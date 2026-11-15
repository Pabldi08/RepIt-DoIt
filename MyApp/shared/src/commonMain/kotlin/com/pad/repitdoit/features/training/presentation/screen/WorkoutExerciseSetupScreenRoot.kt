package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.di.TrainingUseCasesAC
import com.pad.repitdoit.features.training.presentation.layout.WorkoutExerciseSetupLayout
import com.pad.repitdoit.features.training.presentation.viewmodel.WorkoutExerciseSetupViewModel

@Composable
fun WorkoutExerciseSetupScreenRoot(
    appContainer: TrainingUseCasesAC,
    exerciseId: String,
    workoutId: String?,
    onBack: () -> Unit,
    onSaved: (workoutId: String) -> Unit
) {
    val viewModel: WorkoutExerciseSetupViewModel = viewModel {
        WorkoutExerciseSetupViewModel(appContainer, exerciseId, workoutId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val savedWorkoutId = state.selectedWorkout?.id

    LaunchedEffect(state.isSaved, savedWorkoutId) {
        if (state.isSaved && savedWorkoutId != null) {
            onSaved(savedWorkoutId)
        }
    }

    WorkoutExerciseSetupLayout(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

