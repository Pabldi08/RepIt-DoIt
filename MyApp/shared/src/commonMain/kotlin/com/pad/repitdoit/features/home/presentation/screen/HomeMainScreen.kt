package com.pad.repitdoit.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.Header
import com.pad.repitdoit.features.home.presentation.state.HomeUIState
import com.pad.repitdoit.features.home.presentation.components.ProgressCard

// Presenta el contenido principal que el usuario encuentra al entrar.
@Composable
fun HomeMainScreen(
    state: HomeUIState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(modifier = Modifier.fillMaxWidth().height(40.dp))
        ProgressCard(
            progress = state.levelProgress,
            username = state.username,
            loadFailed = state.progressLoadFailed,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
