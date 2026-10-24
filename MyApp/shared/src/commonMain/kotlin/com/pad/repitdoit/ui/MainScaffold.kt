package com.pad.repitdoit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.navigation.NavDestination
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.ui.components.AddExerciseMenu
import com.pad.repitdoit.navigation.AppNavigationItems
import com.pad.repitdoit.navigation.Route

@Composable
fun MainScaffold(
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit,
    showScanOptions: Boolean,
    content: @Composable () -> Unit
) {
    var addMenuExpanded by remember { mutableStateOf(false) }
    var addButtonCenter by remember { mutableStateOf<Offset?>(null) }
    var scaffoldOrigin by remember { mutableStateOf(Offset.Zero) }
    val navigationLayout = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        currentWindowAdaptiveInfo()
    )
    val isNavigationBar = navigationLayout == NavigationSuiteType.NavigationBar

    Box(
        Modifier.fillMaxSize().onGloballyPositioned {
            scaffoldOrigin = it.positionInRoot()
        }
    ) {
        NavigationSuiteScaffold(
            modifier = Modifier.fillMaxSize(),
            layoutType = navigationLayout,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            navigationSuiteItems = {
                AppNavigationItems(
                    showScanOptions = showScanOptions,
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        addMenuExpanded = false
                        onNavigate(route)
                    },
                    addMenuExpanded = addMenuExpanded,
                    onAddClick = {
                        addMenuExpanded = !addMenuExpanded
                    },
                    onAddButtonPositioned = { addButtonCenter = it }
                )
            }
        ) {
            content()
        }

        OnBackHandler(enabled = addMenuExpanded) {
            addMenuExpanded = false
        }

        addButtonCenter?.let { center ->
            AddExerciseMenu(
                showScanOptions = showScanOptions,
                expanded = addMenuExpanded,
                anchorCenter = center - scaffoldOrigin,
                isNavigationBar = isNavigationBar,
                onNfcClick = {
                    addMenuExpanded = false
                    TODO()
                },
                onQrClick = {
                    addMenuExpanded = false
                    TODO()
                },
                onManualClick = {
                    addMenuExpanded = false
                    TODO()
                }
            )
        }
    }
}
