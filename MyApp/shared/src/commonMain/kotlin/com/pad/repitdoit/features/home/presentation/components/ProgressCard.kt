package com.pad.repitdoit.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.gamification.domain.model.LevelProgress
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProgressCard(
    progress: LevelProgress?,
    modifier: Modifier = Modifier,
    loadFailed: Boolean = false,
    username: String? = null
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Tu progreso",
                style = MaterialTheme.typography.titleMedium
            )
            if (progress != null) {
                ProgressCharacter(progress, username)
            }
            if (loadFailed) {
                Text("No se pudo actualizar el progreso")
            } else if (progress == null) {
                Text("Cargando progreso…")
            }
        }
    }
}

@Composable
private fun ProgressCharacter(progress: LevelProgress, username: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(loadImage("placeholder")),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }
            if (!username.isNullOrBlank()) {
                Text(
                    text = "@$username",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier.weight(1.2f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text (
                text = "NIVEL ACTUAL",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = progress.level.toString(),
                style = MaterialTheme.typography.displayLarge,
                fontSize = 72.sp,
                lineHeight =  80.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            LinearProgressIndicator(
                progress = { (progress.currentXp.toDouble() / progress.nextLevelXp).toFloat() },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {}
            )
            Text(
                text = "${progress.currentXp} / ${progress.nextLevelXp} XP",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
