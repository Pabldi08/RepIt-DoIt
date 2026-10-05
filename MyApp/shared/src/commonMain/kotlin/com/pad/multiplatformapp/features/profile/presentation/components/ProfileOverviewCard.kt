package com.pad.multiplatformapp.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun ProfileOverviewCard(
    name: String,
    rank: String,
    streakDays: Int,
    level: Int,
    currentXp: Int,
    nextLevelXp: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileAvatar(name = name)
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("PERFIL", color = ProfileColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    Text(name, color = ProfileColors.Heading, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text(rank, color = ProfileColors.Muted, fontSize = 12.sp)
                    Surface(color = Color(0xFFFFF1E9), shape = RoundedCornerShape(50)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(Modifier.size(6.dp).background(Color(0xFF3A4462), CircleShape))
                            Text("$streakDays días de racha", color = ProfileColors.Heading, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(ProfileColors.Border))
            Spacer(Modifier.height(18.dp))
            LevelCard(level = level, currentXp = currentXp, nextLevelXp = nextLevelXp)
        }
    }
}
