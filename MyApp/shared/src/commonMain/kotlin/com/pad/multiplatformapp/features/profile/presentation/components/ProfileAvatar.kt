package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ProfileAvatar(name: String, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(88.dp).clip(CircleShape)
            .background(Color(0xFFDDE2FF))
            .semantics { contentDescription = "Avatar de $name" }
    ) {
        val w = size.width
        val h = size.height

        val shirt = Path().apply {
            moveTo(0.12f * w, h)
            cubicTo(0.15f * w, 0.76f * h, 0.34f * w, 0.71f * h, 0.5f * w, 0.71f * h)
            cubicTo(0.66f * w, 0.71f * h, 0.85f * w, 0.76f * h, 0.88f * w, h)
            close()
        }
        drawPath(shirt, Color(0xFF2E4D9C))
        drawOval(
            color = Color(0xFFF3BC91),
            topLeft = Offset(0.42f * w, 0.66f * h),
            size = Size(0.16f * w, 0.14f * h)
        )
        drawOval(
            color = Color(0xFFF5C6A0),
            topLeft = Offset(0.30f * w, 0.22f * h),
            size = Size(0.40f * w, 0.53f * h)
        )
        val hair = Path().apply {
            moveTo(0.29f * w, 0.50f * h)
            cubicTo(0.21f * w, 0.26f * h, 0.36f * w, 0.14f * h, 0.53f * w, 0.16f * h)
            cubicTo(0.78f * w, 0.12f * h, 0.82f * w, 0.38f * h, 0.66f * w, 0.42f * h)
            cubicTo(0.51f * w, 0.45f * h, 0.43f * w, 0.36f * h, 0.35f * w, 0.44f * h)
            lineTo(0.33f * w, 0.62f * h)
            cubicTo(0.28f * w, 0.60f * h, 0.27f * w, 0.55f * h, 0.29f * w, 0.50f * h)
            close()
        }
        drawPath(hair, Color(0xFF20365B))
        drawCircle(Color(0xFF20365B), radius = 1.5.dp.toPx(), center = Offset(0.44f * w, 0.53f * h))
        drawCircle(Color(0xFF20365B), radius = 1.5.dp.toPx(), center = Offset(0.59f * w, 0.53f * h))
    }
}
