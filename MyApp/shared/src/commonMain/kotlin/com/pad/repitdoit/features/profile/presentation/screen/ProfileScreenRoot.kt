package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.features.profile.presentation.ProfileViewModel
import androidx.compose.runtime.LaunchedEffect
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun ProfileScreenRoot(windowSize: AppWindowSize, appContainer: AppContainer) {
    val viewModel: ProfileViewModel = viewModel {
        ProfileViewModel(
            appContainer.getProfileUseCase,
            appContainer.getWorkoutHistoryUseCase
        )

    }

    LaunchedEffect(viewModel) {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date

        val fromTimestamp = today.minus(6, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()

        val toTimestamp = today.plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()

        viewModel.loadWorkoutHistory(fromTimestamp, toTimestamp)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(windowSize, uiState)
}

