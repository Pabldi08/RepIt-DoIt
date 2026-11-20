package com.pad.repitdoit.features.gamification.presentation.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.core.ui.common.ReturnButton
import com.pad.repitdoit.features.gamification.presentation.screen.*
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState
import kotlinx.coroutines.launch

// Distribuye el recorrido de progreso y el contenido complementario.
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProgressLayout(
    state: GamificationState = GamificationState(),
    modifier: Modifier = Modifier
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val directive = calculatePaneScaffoldDirective(adaptiveInfo).copy(
        maxHorizontalPartitions = if (isExpanded) 2 else 1,
        maxVerticalPartitions = 1
    )
    val navigator = rememberSupportingPaneScaffoldNavigator<String>(
        scaffoldDirective = directive,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        )
    )
    val paneExpansionState = rememberPaneExpansionState()
    LaunchedEffect(paneExpansionState) {
        paneExpansionState.setFirstPaneProportion(0.6f)
    }
    val scope = rememberCoroutineScope()
    val onNavigate: (String) -> Unit = { destination ->
        scope.launch { navigator.navigateTo(SupportingPaneScaffoldRole.Supporting, destination) }
    }
    val backBehavior = BackNavigationBehavior.PopLatest
    val onBack: () -> Unit = {
        scope.launch { navigator.navigateBack(backBehavior) }
    }
    OnBackHandler(enabled = navigator.canNavigateBack(backBehavior), onBack = onBack)

    SupportingPaneScaffold(
        modifier = modifier.fillMaxSize(),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        paneExpansionState = paneExpansionState,
        mainPane = {
            AnimatedPane {
                if (isExpanded) {
                    ProgressTrailScreen(state, Modifier.fillMaxSize())
                } else {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProgressSummary(state)
                        ProgressTrailScreen(state, Modifier.fillMaxWidth().weight(1f))
                        ProgressActions(state, horizontal = true, onNavigate = onNavigate)
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                val destination = navigator.currentDestination
                    ?.takeIf { it.pane == SupportingPaneScaffoldRole.Supporting }
                    ?.contentKey
                if (destination == null) {
                    ProgressSupportingScreen(state, Modifier.fillMaxSize(), onNavigate)
                } else {
                    Column(Modifier.fillMaxSize().padding(16.dp)) {
                        ReturnButton(onBack = onBack)
                        Text(destination, style = MaterialTheme.typography.headlineSmall)
                        when (destination) {
                            "Logros" -> AchievementScreen(state = state)
                            "Colección" -> RewardsScreen(state = state)
                            "Retos" -> Text("Pantalla de retos pendiente")
                            else -> Text("Contenido pendiente")
                        }
                    }
                }
            }
        }
    )
}
