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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
internal fun ProfileLayout(windowSize: AppWindowSize, state: ProfileUiState) {
    val horizontalPadding = if (windowSize == AppWindowSize.Medium) 32.dp else 20.dp

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding).height(40.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Mi perfil", style = MaterialTheme.typography.titleMediumEmphasized) }
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
                        WorkoutProgressPlaceholder(Modifier.weight(1f))
                    }
                }
            } else {
                item { WorkoutHistorySection(state) }
                item { WorkoutProgressPlaceholder() }
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
private fun WorkoutProgressPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Progreso · Últimos 7 días", style = MaterialTheme.typography.titleMedium)
        OutlinedCard(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Gráfica pendiente",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun historyDayLabel(finishedAt: Long): String {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(timeZone).date
    val sessionDate = Instant.fromEpochMilliseconds(finishedAt)
        .toLocalDateTime(timeZone)
        .date

    if (sessionDate == today) return "Hoy"

    val weekdays = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )
    return weekdays[sessionDate.dayOfWeek.ordinal]
}
