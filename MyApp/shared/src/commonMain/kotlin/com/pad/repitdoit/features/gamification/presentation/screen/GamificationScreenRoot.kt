package com.pad.repitdoit.features.gamification.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.di.GamificationUseCaseAC
import com.pad.repitdoit.features.gamification.presentation.layout.GamificationLayout
import com.pad.repitdoit.features.gamification.presentation.viewmodel.GamificationViewModel

@Composable
fun GamificationScreenRoot(
    appContainer: GamificationUseCaseAC
) {
    val viewModel: GamificationViewModel = viewModel {
        GamificationViewModel(
            appContainer = appContainer
        )
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) { viewModel.refresh() }

    GamificationLayout(
        state = state,
        modifier = Modifier.padding(
            top = 40.dp,
            bottom = 20.dp,
            start = 20.dp,
            end = 20.dp
        )
    )
}
