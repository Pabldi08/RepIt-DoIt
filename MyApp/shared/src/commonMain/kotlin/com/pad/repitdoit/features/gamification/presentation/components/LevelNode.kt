package com.pad.repitdoit.features.gamification.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class LevelStatus {
    COMPLETED,
    CURRENT,
    LOCKED
}

@Composable
fun LevelNode(
    level: Int,
    status: LevelStatus,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    val backgroundColor = when (status) {
        LevelStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        LevelStatus.CURRENT -> MaterialTheme.colorScheme.surfaceVariant
        LevelStatus.LOCKED -> MaterialTheme.colorScheme.surfaceVariant
    }
    val borderColor = when (status) {
        LevelStatus.COMPLETED ->MaterialTheme.colorScheme.primary
        LevelStatus.CURRENT -> MaterialTheme.colorScheme.primary
        LevelStatus.LOCKED -> Color.Gray
    }
    val textColor = when (status) {
        LevelStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimary
        LevelStatus.CURRENT -> MaterialTheme.colorScheme.primary
        LevelStatus.LOCKED -> Color.LightGray
    }
    val nodeSize = 64.dp

    Box (
        modifier = modifier.size(nodeSize),
        contentAlignment = Alignment.Center
    ) {
        Surface (
            modifier = Modifier
                .size(
                    if (status == LevelStatus.CURRENT) nodeSize
                    else 56.dp
                )
                .clickable { onClick(level) },
            shape = CircleShape,
            color = backgroundColor,
            border = borderStroke(
                width = if (status == LevelStatus.CURRENT) 4.dp
                        else 3.dp,
                color = borderColor
            ),
            shadowElevation =   if (status == LevelStatus.CURRENT) 8.dp
                                else 0.dp
        ) {
            Box (
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.toString(),
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}