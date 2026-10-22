package com.pad.repitdoit.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout

@Composable
fun DailyWorkoutCard(
    workout: HomeDailyWorkout,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Entreno de hoy",
            style = MaterialTheme.typography.titleMediumEmphasized
        )
        ActionCard(
            title = workout.name,
            subtitle = "${workout.exerciseCount} ejercicios · ${workout.estimatedTime} min",
            onClick = onClick
        )
    }
}
