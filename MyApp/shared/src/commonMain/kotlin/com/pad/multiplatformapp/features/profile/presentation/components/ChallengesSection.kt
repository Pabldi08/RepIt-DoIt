package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChallengesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Retos", style = MaterialTheme.typography.titleMediumEmphasized)
            Text("Sigue avanzando a tu ritmo", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ChallengeCard(
                number = "01", rewardXp = 150, title = "Perfil completo",
                description = "Añade tus datos", progress = "Completado",
                modifier = Modifier.weight(1f)
            )
            ChallengeCard(
                number = "02", rewardXp = 200, title = "Ritmo semanal",
                description = "Practica 3 días", progress = "2 de 3 días",
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ChallengeCard(
                number = "03", rewardXp = 120, title = "Explora más",
                description = "Abre una lección", progress = "0 de 1 lección",
                modifier = Modifier.weight(1f)
            )
            ChallengeCard(
                number = "04", rewardXp = 250, title = "Constancia",
                description = "Suma 5 sesiones", progress = "3 de 5 sesiones",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
