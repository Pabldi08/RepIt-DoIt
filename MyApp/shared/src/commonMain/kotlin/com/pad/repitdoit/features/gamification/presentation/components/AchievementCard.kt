package com.pad.repitdoit.features.gamification.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.model.AchievementStatus
import com.pad.repitdoit.features.gamification.domain.model.ConditionLogic
import org.jetbrains.compose.resources.painterResource

@Composable
fun AchievementCard(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    var showDetails by rememberSaveable(achievement.id) { mutableStateOf(false) }
    val status = achievement.status
    val colors = MaterialTheme.colorScheme
    OutlinedCard(
        modifier = modifier,
        onClick = { showDetails = true },
        colors = if (status == AchievementStatus.ACHIEVED) {
            CardDefaults.outlinedCardColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        } else {
            CardDefaults.outlinedCardColors()
        },
        border = when (status) {
            AchievementStatus.ACHIEVED,
            AchievementStatus.AVAILABLE -> BorderStroke(1.dp, colors.primary)
            AchievementStatus.LOCKED -> CardDefaults.outlinedCardBorder()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(loadImage(achievement.iconUrl)),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                colorFilter = if (status == AchievementStatus.LOCKED) {
                    ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                } else {
                    null
                }
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(achievement.name)
                when (status) {
                    AchievementStatus.ACHIEVED -> Text("Conseguido")
                    AchievementStatus.AVAILABLE -> AchievementProgress(achievement)
                    AchievementStatus.LOCKED -> Text("Bloqueado")
                }
            }
        }
    }
    if (showDetails) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            title = { Text(achievement.name) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(achievement.desc)
                    when (status) {
                        AchievementStatus.ACHIEVED -> Text("Conseguido")
                        AchievementStatus.AVAILABLE -> AchievementProgress(achievement)
                        AchievementStatus.LOCKED -> Text("Bloqueado")
                    }
                    if (achievement.xpReward > 0) {
                        Text("+${achievement.xpReward} XP")
                    }
                    achievement.rewards.forEach { reward ->
                        Text(reward.name)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDetails = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun AchievementProgress(achievement: Achievement) {
    val progress = achievement.progress
    val goal = achievement.achievementGoal
    if (progress != null && goal != null && goal.isFinite() && goal > 0) {
        val goalText = goal.toString().removeSuffix(".0")
        Text("$progress / $goalText")
        LinearProgressIndicator(
            progress = { (progress / goal).toFloat().coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            drawStopIndicator = {}
        )
    } else if (achievement.conditions.size > 1) {
        val completed = achievement.conditions.count { it.isSatisfied == true }
        val required = when (achievement.conditionLogic) {
            ConditionLogic.ALL -> achievement.conditions.size
            ConditionLogic.ANY -> 1
        }
        val completedRequired = completed.coerceAtMost(required)
        Text("$completedRequired / $required requisitos")
        LinearProgressIndicator(
            progress = { completedRequired.toFloat() / required },
            modifier = Modifier.fillMaxWidth(),
            drawStopIndicator = {}
        )
    } else {
        Text("Progreso no disponible")
    }
}
