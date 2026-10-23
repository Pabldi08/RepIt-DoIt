package com.pad.repitdoit.features.home.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.home.presentation.HomeUIState
import com.pad.repitdoit.features.home.presentation.components.DailyWorkoutCard
import com.pad.repitdoit.features.home.presentation.components.FriendsSection
import com.pad.repitdoit.features.home.presentation.components.ProgressCard
import com.pad.repitdoit.features.home.presentation.components.RecomendationSection
import com.pad.repitdoit.core.ui.common.Header

@Composable
fun HomeCompactLayout(
    uiState: HomeUIState,
    onWorkoutPreview: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp)
        )
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProgressCard() }
            items (
                items = uiState.dailyWorkouts,
                key = { it.id }
            ) { workout ->
                DailyWorkoutCard(
                    workout = workout,
                    onClick = {onWorkoutPreview(workout.id)}
                )
            }
            item { FriendsSection(
                friendName = "Pedro",
                onClick = { }
            ) }
            item { FriendsSection(
                friendName = "Juan",
                onClick = { }
            ) }
            item { FriendsSection(
                friendName = "Maria",
                onClick = { }
            ) }
            item { FriendsSection(
                friendName = "Paula",
                onClick = { }
            ) }
            item { RecomendationSection() }
        }
    }
}
