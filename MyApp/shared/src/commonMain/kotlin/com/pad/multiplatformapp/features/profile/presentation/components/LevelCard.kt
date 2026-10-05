package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LevelCard(level: Int, currentXp: Int, nextLevelXp: Int, modifier: Modifier = Modifier) {
    val progress = if (nextLevelXp > 0) {
        (currentXp.toFloat() / nextLevelXp).coerceIn(0f, 1f)
    } else {
        0f
    }
    val remainingXp = (nextLevelXp - currentXp).coerceAtLeast(0)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Camino al nivel ${level + 1}", color = ProfileColors.Heading, fontSize = 12.sp)
            Text(
                "${(progress * 100).toInt()}%",
                color = ProfileColors.Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(10.dp)
                .background(ProfileColors.ProgressTrack, RoundedCornerShape(50))
                .semantics { progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f) }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(progress).height(10.dp)
                    .background(ProfileColors.Accent, RoundedCornerShape(50))
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "${formatXp(currentXp)} / ${formatXp(nextLevelXp)} XP",
                color = ProfileColors.Heading,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text("${formatXp(remainingXp)} XP para subir", color = ProfileColors.Muted, fontSize = 10.sp)
        }
    }
}

private fun formatXp(value: Int): String =
    value.toString().reversed().chunked(3).joinToString(".").reversed()
