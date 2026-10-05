package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val previewChallenges = listOf(
    ProfileChallenge("01", 150, "Perfil completo", "Añade tus datos", "Completado"),
    ProfileChallenge("02", 200, "Ritmo semanal", "Practica 3 días", "2 de 3 días"),
    ProfileChallenge("03", 120, "Explora más", "Abre una lección", "0 de 1 lección"),
    ProfileChallenge("04", 250, "Constancia", "Suma 5 sesiones", "3 de 5 sesiones")
)

@Composable
fun ChallengesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Retos", color = ProfileColors.Heading, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Sigue avanzando a tu ritmo", color = ProfileColors.Muted, fontSize = 12.sp)
        }
        previewChallenges.chunked(2).forEach { rowChallenges ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowChallenges.forEach { challenge ->
                    ChallengeCard(challenge = challenge, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
