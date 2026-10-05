package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class ProfileChallenge(
    val number: String,
    val rewardXp: Int,
    val title: String,
    val description: String,
    val progress: String
)

@Composable
internal fun ChallengeCard(challenge: ProfileChallenge, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, ProfileColors.Border)
    ) {
        Column(
            modifier = Modifier.heightIn(min = 150.dp).padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = ProfileColors.NumberBackground, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        challenge.number,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                        color = ProfileColors.Accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(color = ProfileColors.RewardBackground, shape = RoundedCornerShape(50)) {
                    Text(
                        "+${challenge.rewardXp} XP",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        color = ProfileColors.RewardText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(challenge.title, color = ProfileColors.Heading, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(challenge.description, color = ProfileColors.Muted, fontSize = 12.sp)
            }
            Text(challenge.progress, color = ProfileColors.Accent, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}
