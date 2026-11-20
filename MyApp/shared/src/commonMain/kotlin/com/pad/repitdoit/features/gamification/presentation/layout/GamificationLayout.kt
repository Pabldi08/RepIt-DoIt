package com.pad.repitdoit.features.gamification.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState

private enum class GamificationTab {
    PROGRESS,
    MUSCLES
}

@Composable
fun GamificationLayout(
    state: GamificationState,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(GamificationTab.PROGRESS.name)
    }
    val stateHolder = rememberSaveableStateHolder()

    Column (
        modifier = modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChip(
                selected = selectedTab == GamificationTab.PROGRESS.name,
                onClick = {
                    selectedTab = GamificationTab.PROGRESS.name
                },
                label = {
                    Text(
                        text = "Progreso"
                    )
                }
            )
            FilterChip(
                selected = selectedTab == GamificationTab.MUSCLES.name,
                onClick = {
                    selectedTab = GamificationTab.MUSCLES.name
                },
                label = {
                    Text(
                        text = "Músculos"
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when(selectedTab) {
                GamificationTab.PROGRESS.name ->
                    ProgressLayout(
                        state = state
                    )
                GamificationTab.MUSCLES.name -> {}
            }
        }
    }
}