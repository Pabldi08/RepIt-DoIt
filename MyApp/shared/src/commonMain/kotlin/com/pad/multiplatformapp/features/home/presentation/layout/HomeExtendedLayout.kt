package com.pad.multiplatformapp.features.home.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.multiplatformapp.features.home.presentation.HomeUIState
import com.pad.multiplatformapp.features.home.presentation.components.DailyRoutineCard
import com.pad.multiplatformapp.features.home.presentation.components.FriendsSection
import com.pad.multiplatformapp.features.home.presentation.components.ProgressCard
import com.pad.multiplatformapp.features.home.presentation.components.RecomendationSection
import com.pad.multiplatformapp.ui.common.Greetings
import com.pad.multiplatformapp.ui.common.Header

@Composable
fun HomeExtendedLayout(
    uiState: HomeUIState,
    onWorkoutPreview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row (

        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(40.dp)
            )
            //SearchBar()

        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProgressCard()
                RecomendationSection()
            }

            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Greetings(name = "Jaime")
                uiState.dailyRoutine?.let { routine ->
                    DailyRoutineCard(
                        routine = routine,
                        onClick = onWorkoutPreview
                    )
                }
                FriendsSection(
                    friendName = "Juan",
                    onClick = { }
                )
            }
        }
    }
}