package com.pad.multiplatformapp.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.multiplatformapp.features.home.presentation.components.FriendsSection
import com.pad.multiplatformapp.features.home.presentation.components.ProgressCard
import com.pad.multiplatformapp.features.home.presentation.components.RecomendationSection
import com.pad.multiplatformapp.features.home.presentation.components.TodayWorkoutSection
import com.pad.multiplatformapp.ui.common.Greetings
import com.pad.multiplatformapp.ui.common.Header

@Composable
fun HomeExtendedLayout() {
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
                var durationMinutes by remember { mutableStateOf(60)}
                Greetings(name = "Jaime")
                TodayWorkoutSection(
                    title = "Tren Superior",
                    exerciseCount = 10,
                    durationMinutes = durationMinutes,
                    onClick = { durationMinutes += 1 }
                )
                FriendsSection(
                    friendName = "Juan",
                    onClick = { }
                )
            }
        }
    }
}