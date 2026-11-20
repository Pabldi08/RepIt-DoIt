package com.pad.repitdoit.features.gamification.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.pad.repitdoit.features.gamification.presentation.components.ProgressPath
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState


// Muestra el recorrido de niveles que puede alcanzar el usuario.
private const val LEVELS_PER_SECTION = 5
private const val TOTAL_SECTIONS = 100

@Composable
fun ProgressTrailScreen(
    state: GamificationState,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier) {

        val safeCurrentLevel = state.level.coerceIn(
            1,
            TOTAL_SECTIONS * LEVELS_PER_SECTION
        )
        val currentSection = (safeCurrentLevel - 1) / LEVELS_PER_SECTION
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = currentSection
        )

        LaunchedEffect(safeCurrentLevel) {
            listState.scrollToItem(currentSection)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            reverseLayout = true
        ) {
            items(
                count = TOTAL_SECTIONS,
                key = { section -> section}
            ) { section ->
                ProgressPath(
                    sectionIndex = section,
                    currentLevel = safeCurrentLevel
                )
            }
        }
    }
}
