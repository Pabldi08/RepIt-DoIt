package com.pad.repitdoit.features.gamification.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private const val LEVELS_PER_SECTION = 5

private val nodeSize = 64.dp
private val nodeSpacing = 104.dp

private fun levelXFraction(
    level: Int
) : Float {
    return when((level - 1) % 4) {
        0 -> 0.50f
        1 -> 0.28f
        2 -> 0.50f
        else -> 0.72f
    }
}

@Composable
fun ProgressPath(
    sectionIndex: Int,
    currentLevel: Int,
    modifier: Modifier = Modifier,
    onLevelClick: (Int) -> Unit = {}
) {
    val firstLevel = sectionIndex * LEVELS_PER_SECTION + 1
    val sectionHeight = nodeSpacing * LEVELS_PER_SECTION
    val pathColor = MaterialTheme.colorScheme.outlineVariant

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(sectionHeight)
    ) {
        val availableWidth = maxWidth
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .clipToBounds()
        ) {
            val spacingPx = nodeSpacing.toPx()

            fun x(level: Int): Float =
                size.width * levelXFraction(level.coerceAtLeast(1))
            fun y(index: Int): Float =
                size.height - (index + 0.5f) * spacingPx

            for (index in -1 until LEVELS_PER_SECTION) {
                if (sectionIndex == 0 && index == -1) continue

                val startLevel = firstLevel + index
                val endLevel = startLevel + 1

                val startX = x(startLevel)
                val startY = y(index)
                val endX = x(endLevel)
                val endY = y(index + 1)
                val middleY = (startY + endY) / 2f

                val segment = Path().apply {
                    moveTo(startX, startY)
                    cubicTo(
                        startX, middleY,
                        endX, middleY,
                        endX, endY
                    )
                }

                drawPath(
                    path = segment,
                    color = pathColor,
                    style = Stroke(
                        width = 22.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        repeat(LEVELS_PER_SECTION) { index ->
            val level = firstLevel + index
            val status = when {
                level < currentLevel -> LevelStatus.COMPLETED
                level == currentLevel -> LevelStatus.CURRENT
                else -> LevelStatus.LOCKED
            }
            val x = availableWidth * levelXFraction(level) - nodeSize/2
            val y = sectionHeight - nodeSpacing * (index + 0.5f) - nodeSize/2

            LevelNode(
                level = level,
                status = status,
                modifier = Modifier.offset(x, y),
                onClick = onLevelClick
            )
        }
    }
}