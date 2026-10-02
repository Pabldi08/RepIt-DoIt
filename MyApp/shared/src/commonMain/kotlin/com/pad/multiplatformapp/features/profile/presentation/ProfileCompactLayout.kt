package com.pad.multiplatformapp.features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.pad.multiplatformapp.ui.common.Header

@Composable
fun     ProfileCompactLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Borrar luego
        var durationMinutes by remember { mutableStateOf(60) }

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

            item { RecomendationSection() }
        }
    }
}