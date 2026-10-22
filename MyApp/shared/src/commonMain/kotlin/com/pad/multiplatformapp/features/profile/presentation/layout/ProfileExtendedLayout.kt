 package com.pad.multiplatformapp.features.profile.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.multiplatformapp.features.profile.presentation.components.ProfileOverviewCard
import com.pad.multiplatformapp.ui.common.Header

@Composable
fun ProfileExtendedLayout() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(40.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Mi progreso", style = MaterialTheme.typography.titleMediumEmphasized) }
            item {
                ProfileOverviewCard(
                    name = "Alex Rivera",
                    rank = "Nivel 3 · Explorador",
                    streakDays = 7,
                    level = 3,
                    currentXp = 1280,
                    nextLevelXp = 2000
                )
            }
        }
    }
}
