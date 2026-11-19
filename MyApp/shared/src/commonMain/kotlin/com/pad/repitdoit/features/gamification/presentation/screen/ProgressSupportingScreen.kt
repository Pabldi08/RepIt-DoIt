package com.pad.repitdoit.features.gamification.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProgressSupportingScreen(
    state: GamificationState,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProgressSummary(state)
        ProgressActions(state = state, onNavigate = onNavigate)
    }
}

@Composable
fun ProgressSummary(
    state: GamificationState,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tu progreso",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Nivel ${state.level}",
                style = MaterialTheme.typography.headlineSmall
            )
            val xp = state.currentXp
            val goal = state.nextLevelXp
            LinearProgressIndicator(
                progress = {
                    if (goal > 0) {
                        (xp.toDouble() / goal).toFloat().coerceIn(0f, 1f)
                    } else 0f
                },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {}
            )
            Text("${xp} / ${goal} XP")
        }
    }
}

private data class ProgressAction(
    val title: String,
    val icon: String,
    val obtained: Int,
    val total: Int
)
@Composable
fun ProgressActions(
    state: GamificationState,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
    onNavigate: (String) -> Unit = {}
) {
    val destinations = listOf(
        ProgressAction(
            title = "Logros",
            icon = "iconAchievement",
            obtained = state.obtainedAchievementCount,
            total = state.achievementCount
        ),
        ProgressAction(
            title = "Retos",
            icon = "iconGoal",
            obtained = 0,
            total = 0
        ),
        ProgressAction(
            title = "Colección",
            icon = "iconCollectible",
            obtained = state.ownedCollectibleCount,
            total = state.collectibleCount
        )
    )
    if (horizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            destinations.forEach { action ->
                OutlinedButton(
                    enabled = action.total != 0,
                    onClick = { onNavigate(action.title) },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 12.dp)
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(loadImage(action.icon)),
                            contentDescription = null
                        )
                        Text(
                            text = action.title
                        )
                        Text (
                            text = if (action.total != 0) "${action.obtained}/${action.total}"
                                    else "En camino"
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            destinations.forEach { action ->
                OutlinedButton(
                    enabled = action.total != 0,
                    onClick = { onNavigate(action.title) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(loadImage(action.icon)),
                            contentDescription = null
                        )
                        Text(if (action.title == "Colección") "Coleccionables" else action.title)
                        Text (
                            text = if (action.total != 0) "${action.obtained}/${action.total}"
                            else "En camino"
                        )
                    }
                }
            }
        }
    }
}
