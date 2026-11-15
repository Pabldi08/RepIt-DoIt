package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.di.TrainingUseCasesAC
import com.pad.repitdoit.features.training.presentation.viewmodel.TrainingViewModel
import com.pad.repitdoit.features.training.presentation.layout.TrainingLayout

@Composable
fun TrainingScreenRoot(
    appContainer: TrainingUseCasesAC,
    initialWorkoutId: String? = null,
    onAddExercise: (workoutId: String) -> Unit
) {
    val viewModel: TrainingViewModel = viewModel {
        TrainingViewModel(
            appContainer = appContainer
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    //Refresh state data every time this viewModel regains focus
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    when {
        uiState.isLoadingInitialData -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text("Cargando entrenamientos…", modifier = Modifier.padding(top = 16.dp))
            }
        }
        uiState.workouts.isEmpty() && uiState.plans.isEmpty() &&
            (uiState.plansError != null || uiState.errorMessage != null) -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.errorMessage?.let { Text(it) }
                uiState.plansError?.let { Text(it) }
                if (uiState.isRefreshing) CircularProgressIndicator()
                TextButton(enabled = !uiState.isRefreshing, onClick = viewModel::refresh) {
                    Text("Reintentar")
                }
            }
        }
        else -> {
            Box(Modifier.fillMaxSize()) {
                TrainingLayout(
                    state = uiState,
                    initialWorkoutId = initialWorkoutId,
                    onAction = viewModel::onAction,
                    onAddExercise = onAddExercise
                )
                if (uiState.isRefreshing) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}
