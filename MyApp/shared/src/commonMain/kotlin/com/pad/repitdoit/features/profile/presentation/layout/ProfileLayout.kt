package com.pad.repitdoit.features.profile.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.features.profile.presentation.components.ProfileOverviewCard
import com.pad.repitdoit.features.profile.presentation.ProfileUiState
import com.pad.repitdoit.core.ui.common.Header
import com.pad.repitdoit.features.profile.presentation.components.WorkoutHistoryCard
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.SolidColor
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisContent
import io.github.koalaplot.core.xygraph.rememberFloatLinearAxisModel
import kotlinx.datetime.number
import androidx.compose.foundation.layout.Spacer

// Distribuye la información del perfil, el progreso semanal y el historial.
@Composable
internal fun ProfileLayout(
    windowSize: AppWindowSize,
    state: ProfileUiState,
    onLogout: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val horizontalPadding = if (windowSize == AppWindowSize.Medium) 32.dp else 20.dp


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Header(modifier = Modifier.weight(1f).height(40.dp))
            TextButton(onClick = onOpenSettings) { Text("Ajustes") }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    "Mi perfil",
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
            }

            item {
                val profile = state.profile

                if (profile == null) {
                    Text("Cargando perfil…")
                } else {
                    ProfileOverviewCard(
                        name = profile.displayName,
                        username = profile.username,
                        totalXp = state.totalXp
                    )
                }
            }
            if (windowSize == AppWindowSize.Extended) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        WorkoutHistorySection(state, Modifier.weight(1f))
                        WorkoutProgressCard(state, Modifier.weight(1f))
                    }
                }
            } else {
                item { WorkoutHistorySection(state) }
                item { WorkoutProgressCard(state) }
            }

            item {

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoggingOut,
                    onClick = onLogout
                ) {
                    Text(
                        if (state.isLoggingOut)
                            "Cerrando sesión..."
                        else
                            "Cerrar sesión",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.error?.let { error ->
                item {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
@Composable
private fun WorkoutHistorySection(state: ProfileUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Historial", style = MaterialTheme.typography.titleMedium)

        val latestSession = state.workoutHistory.firstOrNull()
        when {
            state.isHistoryLoading -> Text("Cargando historial…")
            state.historyError != null -> Text(state.historyError)
            latestSession == null -> Text("Sin entrenamientos en los últimos 7 días.")
            else -> WorkoutHistoryCard(
                entry = latestSession,
                dayLabel = historyDayLabel(latestSession.finishedAt)
            )
        }
    }
}

@Composable
private fun WorkoutProgressCard(
    state: ProfileUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Progreso de los últimos 7 días",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when {
                    state.isHistoryLoading -> Text("Cargando progreso…")
                    state.historyError != null -> Text("No se pudo cargar el progreso.")
                    state.weeklyWorkoutProgress.isEmpty() -> Text("Sin datos de progreso.")
                    else -> {
                        val week = state.weeklyWorkoutProgress
                        val points = remember(week) {
                            week.mapIndexed { index, day ->
                                DefaultPoint((index + 1).toFloat(), day.minutes)
                            }
                        }

                        val maxMinutes = week.maxOf { it.minutes }
                        val axisMax = kotlin.math.ceil(maxMinutes / 60f)
                            .coerceAtLeast(1f) * 60f

                        XYGraph(
                            xAxisModel = rememberFloatLinearAxisModel(
                                range = 1f..7f,
                                minimumMajorTickIncrement = 1f,
                                minorTickCount = 0
                            ),
                            yAxisModel = rememberFloatLinearAxisModel(
                                range = 0f..axisMax,
                                minimumMajorTickIncrement = axisMax / 4f,
                                minorTickCount = 0
                            ),
                            xAxisContent = rememberAxisContent(
                                labels = { position ->
                                    val date = week.getOrNull(position.toInt() - 1)?.date
                                    if (date != null) {
                                        Text(
                                            text = "${date.day}/${date.month.number}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            ),
                            yAxisContent = rememberAxisContent(
                                labels = { minutes ->
                                    Text(
                                        text = "${minutes.toInt()} min",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            ),
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        ) {
                            LinePlot(
                                data = points,
                                lineStyle = LineStyle(
                                    brush = SolidColor(MaterialTheme.colorScheme.primary),
                                    strokeWidth = 2.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun historyDayLabel(finishedAt: Long): String {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(timeZone).date
    val sessionDate = Instant.fromEpochMilliseconds(finishedAt)
        .toLocalDateTime(timeZone)
        .date

    // Muestra «Hoy» para distinguir las sesiones de la fecha actual.
    if (sessionDate == today) return "Hoy"

    val weekdays = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )
    return weekdays[sessionDate.dayOfWeek.ordinal]
}