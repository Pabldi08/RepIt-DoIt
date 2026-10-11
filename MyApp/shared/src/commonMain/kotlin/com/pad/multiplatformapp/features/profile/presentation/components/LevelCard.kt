package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            Text("Camino al nivel ${level + 1}", style = MaterialTheme.typography.bodySmall)
            Text(
                "${(progress * 100).toInt()}%",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(10.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(progress).height(10.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "$currentXp / $nextLevelXp XP",
                style = MaterialTheme.typography.labelSmall
            )
            Text("$remainingXp XP para subir", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
    }
}
