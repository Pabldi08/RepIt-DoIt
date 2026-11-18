package com.pad.repitdoit.features.gamification.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.gamification.domain.model.Reward
import org.jetbrains.compose.resources.painterResource

@Composable
fun RewardCard(
    reward: Reward,
    modifier: Modifier = Modifier
) {
    var showDetails by rememberSaveable(reward.id) { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier,
        onClick = { showDetails = true }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(loadImage(reward.assetUrl)),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                colorFilter = if (reward.isOwned) null
                                else ColorFilter
                                    .colorMatrix(
                                        ColorMatrix().apply{
                                            setToSaturation(0f)
                                        }
                                    )
            )
            Text(
                text = reward.name,
                textAlign = TextAlign.Center
            )
            if (reward.isOwned) {
                Text(
                    text = "OBTAINED AT ${reward.obtainedAt}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showDetails) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            title = { Text(reward.name) },
            text = {
                Text(reward.description ?: if (reward.isOwned) "Conseguido" else "Sin conseguir")
            },
            confirmButton = {
                TextButton(onClick = { showDetails = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}
