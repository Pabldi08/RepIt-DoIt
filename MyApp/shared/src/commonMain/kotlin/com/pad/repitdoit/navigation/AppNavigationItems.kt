package com.pad.repitdoit.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.iconDumbbell
import repitdoit.shared.generated.resources.iconHouse
import repitdoit.shared.generated.resources.iconProfile
import repitdoit.shared.generated.resources.iconScanner

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
