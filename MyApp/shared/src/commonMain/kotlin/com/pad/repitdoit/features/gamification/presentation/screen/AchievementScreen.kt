package com.pad.repitdoit.features.gamification.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.features.gamification.domain.model.AchievementStatus
import com.pad.repitdoit.features.gamification.presentation.components.AchievementCard
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState

@Composable
fun AchievementScreen(
    state: GamificationState
) {
    val achievements = remember(state.achievements) {
        state.achievements.sortedBy { achievement ->
            when (achievement.status) {
                AchievementStatus.AVAILABLE -> 0
                AchievementStatus.ACHIEVED -> 1
                AchievementStatus.LOCKED -> 2
            }
        }
    }
    val obtainedCount = state.obtainedAchievementCount
    val totalCount = state.achievementCount
    val progress = if (totalCount > 0) obtainedCount.toFloat() / totalCount else 0f

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "$obtainedCount de $totalCount logros conseguidos",
                style = MaterialTheme.typography.bodyMedium
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {}
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(achievements, key = { it.id }) { achievement ->
                AchievementCard(
                    achievement = achievement,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
