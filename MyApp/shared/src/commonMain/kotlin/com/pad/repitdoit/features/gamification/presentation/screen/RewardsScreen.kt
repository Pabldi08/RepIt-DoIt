package com.pad.repitdoit.features.gamification.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.gamification.presentation.components.RewardCard
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState

@Composable
fun RewardsScreen(
    state: GamificationState
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 96.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.collectibles) { reward ->
            RewardCard(
                reward = reward,
                modifier = Modifier.aspectRatio(1f)
            )
        }
    }
}