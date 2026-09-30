package ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayWorkoutSection(
    title: String,
    exerciseCount: Int,
    durationMinutes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Entreno de hoy",
            style = MaterialTheme.typography.titleMediumEmphasized
        )
        ActionCard(
            title = title,
            subtitle = "$exerciseCount ejercicios · $durationMinutes min",
            onClick = onClick
        )
    }
}
