package com.pad.repitdoit.features.profile.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.AppWindowSize
import com.pad.repitdoit.features.profile.presentation.components.ProfileOverviewCard
import com.pad.repitdoit.features.profile.presentation.ProfileUiState
import com.pad.repitdoit.core.ui.common.Header

@Composable
internal fun ProfileLayout(windowSize: AppWindowSize, state: ProfileUiState) {
    val horizontalPadding = if (windowSize == AppWindowSize.Medium) 32.dp else 20.dp

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding).height(40.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Mi perfil", style = MaterialTheme.typography.titleMediumEmphasized) }
            item {
                val profile = state.profile
                if (profile == null) {
                    Text("Cargando perfil…")
                } else {
                    ProfileOverviewCard(
                        name = profile.displayName,
                        username = profile.username
                    )
                }
            }
        }
    }
}
