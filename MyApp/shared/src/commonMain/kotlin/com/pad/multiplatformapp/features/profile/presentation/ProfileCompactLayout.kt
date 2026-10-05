package com.pad.multiplatformapp.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pad.multiplatformapp.features.profile.presentation.components.ChallengesSection
import com.pad.multiplatformapp.features.profile.presentation.components.ProfileColors
import com.pad.multiplatformapp.features.profile.presentation.components.ProfileOverviewCard
import com.pad.multiplatformapp.ui.common.Header

@Composable
fun ProfileCompactLayout(maxContentWidth: Dp = 480.dp) {
    Box(
        modifier = Modifier.fillMaxSize().background(ProfileColors.Background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.widthIn(max = maxContentWidth).fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Header(modifier = Modifier.fillMaxWidth().height(40.dp))
            }
            item {
                Text(
                    text = "Mi progreso",
                    color = ProfileColors.Heading,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                ProfileOverviewCard(
                    name = "Alex Rivera",
                    rank = "Nivel 3 · Explorador",
                    streakDays = 7,
                    level = 3,
                    currentXp = 1280,
                    nextLevelXp = 2000
                )
            }
            item { ChallengesSection() }
        }
    }
}
