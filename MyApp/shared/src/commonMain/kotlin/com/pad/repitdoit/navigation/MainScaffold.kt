package com.pad.repitdoit.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.pad.repitdoit.core.ui.common.NfcInfoPanel
import com.pad.repitdoit.core.ui.common.OnBackHandler
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.addManually
import repitdoit.shared.generated.resources.addNFC
import repitdoit.shared.generated.resources.addQR
import repitdoit.shared.generated.resources.iconDumbbell
import repitdoit.shared.generated.resources.iconHouse
import repitdoit.shared.generated.resources.iconProfile
import repitdoit.shared.generated.resources.iconScanner
import repitdoit.shared.generated.resources.placeholder
import kotlin.math.roundToInt

@Composable
fun MainScaffold(
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit,
    showScanOptions: Boolean,
    onQrClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var addMenuExpanded by remember { mutableStateOf(false) }
    var addButtonCenter by remember { mutableStateOf<Offset?>(null) }
    var nfcPanelVisible by remember { mutableStateOf(false) }
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

        OnBackHandler(enabled = addMenuExpanded || nfcPanelVisible) {
            if (nfcPanelVisible) {
                nfcPanelVisible = false
            } else {
                addMenuExpanded = false
            }
        }

        addButtonCenter?.let { center ->
            AddExerciseMenu(
                showScanOptions = showScanOptions,
                expanded = addMenuExpanded,
                anchorCenter = center - scaffoldOrigin,
                isNavigationBar = isNavigationBar,
                onNfcClick = {
                    addMenuExpanded = false
                    nfcPanelVisible = true
                },
                onQrClick = {
                    addMenuExpanded = false
                    onQrClick()
                },
                onManualClick = {
                    addMenuExpanded = false
                    TODO()
                }
            )
        }
        NfcInfoPanel(
            visible = showScanOptions && nfcPanelVisible,
            onDismiss = { nfcPanelVisible = false },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

fun NavigationSuiteScope.AppNavigationItems(
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit,
    addMenuExpanded: Boolean,
    showScanOptions: Boolean,
    onAddClick: () -> Unit,
    onAddButtonPositioned: (Offset) -> Unit
) {
    item (
        selected = currentDestination?.hasRoute<Route.Home>() == true,
        onClick = { onNavigate(Route.Home) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconHouse),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Home") }
    )
    item (
        selected = currentDestination?.hasRoute<Route.Training>() == true,
        onClick = { onNavigate(Route.Training) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconDumbbell),
                contentDescription = "Training",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Training") }
    )
    item(
        selected = false,
        onClick = onAddClick,
        icon = {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .onGloballyPositioned {
                        onAddButtonPositioned(it.boundsInRoot().center)
                    }
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .semantics {
                        contentDescription =
                            if (addMenuExpanded) "Close Menu"
                            else "Add exercise"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    )
    if (showScanOptions) {
        item (
            selected = currentDestination?.hasRoute<Route.Scanner>() == true,
            onClick = { onNavigate(Route.Scanner) },
            icon = {
                Icon (
                    painter = painterResource(Res.drawable.iconScanner),
                    contentDescription = "Scanner",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text ("Scanner") }
        )
    }
    item (
        selected = currentDestination?.hasRoute<Route.Profile>() == true,
        onClick = { onNavigate(Route.Profile) },
        icon = {
            Icon (
                painter = painterResource(Res.drawable.iconProfile),
                contentDescription = "Profile",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Profile") }
    )
}

@Composable
fun ScannerScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.placeholder),
                null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( "Scanner screen" )
        }
    }
}


@Composable
private fun AddExerciseMenu(
    expanded: Boolean,
    showScanOptions: Boolean,
    anchorCenter: Offset,
    isNavigationBar: Boolean,
    onNfcClick: () -> Unit,
    onQrClick: () -> Unit,
    onManualClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    var menuSize by remember { mutableStateOf(IntSize.Zero) }
    val progress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "Add menu expansion"
    )
    if (progress > 0f) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = AbsoluteAlignment.TopLeft
        ) {
            Row(
                modifier = Modifier
                    .onSizeChanged { menuSize = it }
                    .absoluteOffset {
                        // Position the whole row relative to the + button.
                        val clearance = 38.dp.toPx() // Button radius (22dp) + gap (16dp).
                        val x = when {
                            isNavigationBar -> anchorCenter.x - menuSize.width / 2f
                            isLtr -> anchorCenter.x + clearance
                            else -> anchorCenter.x - clearance - menuSize.width
                        }
                        val y = if (isNavigationBar) {
                            anchorCenter.y - clearance - menuSize.height
                        } else {
                            anchorCenter.y - menuSize.height / 2f
                        }
                        IntOffset(x.roundToInt(), y.roundToInt())
                    }
                    .graphicsLayer {
                        alpha = progress
                        scaleX = progress
                        scaleY = progress
                        transformOrigin = when {
                            isNavigationBar -> TransformOrigin(0.5f, 1f)
                            isLtr -> TransformOrigin(0f, 0.5f)
                            else -> TransformOrigin(1f, 0.5f)
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (showScanOptions) {
                    AddMenuAction(
                        text = "NFC",
                        icon = Res.drawable.addNFC,
                        enabled = expanded,
                        onClick = onNfcClick
                    )
                    AddMenuAction(
                        text = "QR",
                        icon = Res.drawable.addQR,
                        enabled = expanded,
                        onClick = onQrClick
                    )
                }
                AddMenuAction(
                    text = "Add manually",
                    icon = Res.drawable.addManually,
                    enabled = expanded,
                    onClick = onManualClick
                )
            }
        }
    }
}

@Composable
private fun AddMenuAction(
    text: String,
    icon: DrawableResource,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        SmallFloatingActionButton(
            onClick = {
                if (enabled) onClick()
            },
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}