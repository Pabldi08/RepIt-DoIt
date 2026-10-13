package com.pad.multiplatformapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Background = Color(0xFF0E100F)
private val Surface = Color(0xFF171A18)
private val Elevated = Color(0xFF212522)
private val Accent = Color(0xFFADF238)
private val Text = Color(0xFFF0F2F0)
private val SecondaryText = Color(0xFF9EA6A1)

private val AppColors = darkColorScheme(
    primary = Accent,
    onPrimary = Background,
    primaryContainer = Elevated,
    onPrimaryContainer = Accent,
    inversePrimary = Color(0xFF426600),
    secondary = SecondaryText,
    onSecondary = Background,
    secondaryContainer = Elevated,
    onSecondaryContainer = Text,
    tertiary = Accent,
    onTertiary = Background,
    tertiaryContainer = Elevated,
    onTertiaryContainer = Accent,
    background = Background,
    onBackground = Text,
    surface = Surface,
    onSurface = Text,
    surfaceVariant = Elevated,
    onSurfaceVariant = SecondaryText,
    surfaceTint = Accent,
    surfaceDim = Background,
    surfaceBright = Elevated,
    surfaceContainerLowest = Background,
    surfaceContainerLow = Surface,
    surfaceContainer = Surface,
    surfaceContainerHigh = Elevated,
    surfaceContainerHighest = Elevated,
    inverseSurface = Text,
    inverseOnSurface = Background,
    outline = SecondaryText,
    outlineVariant = Color(0xFF343B36),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    scrim = Color.Black
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}
