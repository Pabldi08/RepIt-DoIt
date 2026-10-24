package com.pad.repitdoit.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.addManually
import repitdoit.shared.generated.resources.addNFC
import repitdoit.shared.generated.resources.addQR
import kotlin.math.roundToInt


@Composable
internal fun AddExerciseMenu(
    expanded: Boolean,
    showScanOptions: Boolean,
    anchorCenter: Offset,
    isNavigationBar: Boolean,
    onNfcClick: () -> Unit,
    onQrClick: () -> Unit,
    onManualClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    var menuSize by remember { mutableStateOf(IntSize.Zero) }
    val progress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "Add menu expansion"
    )
    if (progress > 0f) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = AbsoluteAlignment.TopLeft
        ) {
            Row(
                modifier = Modifier
                    .onSizeChanged { menuSize = it }
                    .absoluteOffset {
                        // Position the whole row relative to the + button.
                        val clearance = 38.dp.toPx() // Button radius (22dp) + gap (16dp).
                        val x = when {
                            isNavigationBar -> anchorCenter.x - menuSize.width / 2f
                            isLtr -> anchorCenter.x + clearance
                            else -> anchorCenter.x - clearance - menuSize.width
                        }
                        val y = if (isNavigationBar) {
                            anchorCenter.y - clearance - menuSize.height
                        } else {
                            anchorCenter.y - menuSize.height / 2f
                        }
                        IntOffset(x.roundToInt(), y.roundToInt())
                    }
                    .graphicsLayer {
                        alpha = progress
                        scaleX = progress
                        scaleY = progress
                        transformOrigin = when {
                            isNavigationBar -> TransformOrigin(0.5f, 1f)
                            isLtr -> TransformOrigin(0f, 0.5f)
                            else -> TransformOrigin(1f, 0.5f)
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (showScanOptions) {
                    AddMenuAction(
                        text = "NFC",
                        icon = Res.drawable.addNFC,
                        enabled = expanded,
                        onClick = onNfcClick
                    )
                    AddMenuAction(
                        text = "QR",
                        icon = Res.drawable.addQR,
                        enabled = expanded,
                        onClick = onQrClick
                    )
                }
                AddMenuAction(
                    text = "Add manually",
                    icon = Res.drawable.addManually,
                    enabled = expanded,
                    onClick = onManualClick
                )
            }
        }
    }
}

@Composable
private fun AddMenuAction(
    text: String,
    icon: DrawableResource,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        SmallFloatingActionButton(
            onClick = {
                if (enabled) onClick()
            },
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
