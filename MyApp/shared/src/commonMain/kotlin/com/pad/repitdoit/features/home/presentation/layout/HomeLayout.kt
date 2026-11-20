package com.pad.repitdoit.features.home.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import com.pad.repitdoit.features.home.presentation.state.HomeUIState
import com.pad.repitdoit.features.home.presentation.components.DailyWorkoutCard
import com.pad.repitdoit.features.home.presentation.screen.HomeMainScreen
import com.pad.repitdoit.features.home.presentation.screen.HomeSupportingScreen

// Distribuye el contenido principal y complementario de inicio.
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeLayout(
    uiState: HomeUIState,
    onWorkoutPreview: (String) -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val directive = calculatePaneScaffoldDirective(adaptiveInfo).copy(
        maxHorizontalPartitions = if (isExpanded) 2 else 1,
        maxVerticalPartitions = 1
    )
    // Organiza el contenido principal y el panel complementario con el navegador adaptable.
    val navigator = rememberSupportingPaneScaffoldNavigator<Nothing>(
        scaffoldDirective = directive,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        )
    )
    val paneExpansionState = rememberPaneExpansionState()
    LaunchedEffect(paneExpansionState) {
        paneExpansionState.setFirstPaneProportion(0.6f)
    }
    val contentPadding = PaddingValues(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp)

    SupportingPaneScaffold(
        modifier = Modifier.fillMaxSize(),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        paneExpansionState = paneExpansionState,
        mainPane = {
            AnimatedPane {
                if (isExpanded) {
                    HomeMainScreen(
                        state = uiState,
                        modifier = Modifier.fillMaxSize().padding(contentPadding)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            HomeMainScreen(state = uiState, modifier = Modifier.fillMaxWidth())
                        }
                        if (uiState.dailyWorkouts.isNotEmpty()) {
                            item {
                                Text(
                                    "Entrenos de hoy",
                                    style = MaterialTheme.typography.titleMediumEmphasized
                                )
                            }
                        }
                        items(uiState.dailyWorkouts) { workout ->
                            DailyWorkoutCard(
                                workout = workout,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onWorkoutPreview(workout.id) }
                            )
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                HomeSupportingScreen(
                    state = uiState,
                    onWorkoutPreview = onWorkoutPreview
                )
            }
        }
    )
}
