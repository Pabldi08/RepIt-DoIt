package com.pad.repitdoit.features.home.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pad.repitdoit.features.home.domain.model.HomeDailyWorkout

@Composable
fun DailyWorkoutCard(
    workout: HomeDailyWorkout,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ActionCard(
        title = workout.name,
        subtitle = "${workout.exerciseCount} ejercicios",
        modifier = modifier,
        onClick = onClick
    )
}
