package com.pad.repitdoit.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.home.presentation.state.HomeUIState
import com.pad.repitdoit.features.home.presentation.components.DailyWorkoutCard

// Agrupa el contenido complementario de la pantalla de inicio.
@Composable
fun HomeSupportingScreen(
    state: HomeUIState,
    modifier: Modifier = Modifier,
    onWorkoutPreview: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.dailyWorkouts.isNotEmpty()) {
            item {
                Text(
                    "Entrenos de hoy",
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
            }
        }
        items(state.dailyWorkouts) { workout ->
            DailyWorkoutCard(
                workout = workout,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onWorkoutPreview(workout.id) }
            )
        }
    }
}