package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.getWindowSize
import org.jetbrains.compose.resources.painterResource

// Muestra la duración y el número de ejercicios de la sesión completada.
@Composable
internal fun WorkoutSessionSummaryScreen(workout: Workout, durationSeconds: Int, onContinue: () -> Unit) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val window = LocalWindowInfo.current.containerSize
    val isLandscape = window.width > window.height
    val posture = adaptiveInfo.windowPosture
    val flex = isLandscape && posture.isTabletop
    val isWideWindow = getWindowSize(adaptiveInfo.windowSizeClass) != AppWindowSize.Compact
    val wide = isWideWindow || isLandscape
    val hinge = posture.hingeList.firstOrNull { !it.isFlat && !it.isVertical }
    val density = LocalDensity.current
    var windowTop by remember { mutableFloatStateOf(Float.NaN) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .onGloballyPositioned { windowTop = it.positionInWindow().y }
    ) {
        val summary: @Composable () -> Unit = { SummaryCard(durationSeconds, workout.workoutExercises.size) }
        val continueButton: @Composable () -> Unit = {
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Continuar") }
        }

        when {
            flex -> {
                val upperHeight = hinge?.takeIf { windowTop.isFinite() }?.let {
                    with(density) { (it.bounds.top - windowTop).toDp() }.coerceIn(0.dp, maxHeight)
                } ?: (maxHeight / 2)
                val gap = with(density) { (hinge?.bounds?.height ?: 0f).toDp() }
                    .coerceIn(0.dp, maxHeight - upperHeight)
                Column(Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxWidth().height(upperHeight).verticalScroll(rememberScrollState())
                            .padding(horizontal = 40.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        SummaryHeading(workout.name, centered = false)
                        summary()
                    }
                    Spacer(Modifier.height(gap))
                    Box(
                        Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())
                            .padding(horizontal = 40.dp, vertical = 24.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) { continueButton() }
                }
            }

            wide -> Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SummaryHeading(workout.name, centered = false)
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(Modifier.weight(2f)) { summary() }
                    Box(Modifier.weight(3f)) { continueButton() }
                }
            }

            else -> Column(
                Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    SummaryHeading(workout.name, centered = true)
                    summary()
                }
                continueButton()
            }
        }
    }
}

@Composable
private fun SummaryHeading(workoutName: String, centered: Boolean) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (centered) {
            Box(Modifier.size(58.dp), contentAlignment = Alignment.Center) {
                Image(painterResource(loadImage("training_summary_success")), null, Modifier.matchParentSize())
                Text("✓", color = colors.primary, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(
            if (centered) "Entrenamiento completado" else "✓  Entrenamiento completado",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start
        )
        Text(workoutName, color = colors.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SummaryCard(durationSeconds: Int, exerciseCount: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Resumen", style = MaterialTheme.typography.titleMedium)
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            val duration = if (minutes == 0) "$seconds s" else "$minutes min $seconds s"
            SummaryRow("Duración", duration)
            SummaryRow("Ejercicios", "$exerciseCount completados")
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
