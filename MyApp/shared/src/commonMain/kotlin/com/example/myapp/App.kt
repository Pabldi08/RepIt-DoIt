package com.example.myapp

import androidx.annotation.ColorInt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import org.jetbrains.compose.resources.painterResource

import myapp.shared.generated.resources.Res
import myapp.shared.generated.resources.compose_multiplatform
import myapp.shared.generated.resources.iconDumbbell
import myapp.shared.generated.resources.iconHouse
import myapp.shared.generated.resources.iconLogo1
import myapp.shared.generated.resources.iconProfile
import myapp.shared.generated.resources.iconScanner
import myapp.shared.generated.resources.logo
import myapp.shared.generated.resources.placeholder
import ui.home.HomeScreen


enum class AppScreen {
    Home,
    Training,
    Scanner,
    Profile
}

enum class AppWindowSize {
    Compact,
    Medium,
    Extended
}

@Composable
@Preview
fun App() {
    var selectedTab by remember {mutableStateOf(AppScreen.Home)}
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowSize = getWindowSize(windowSizeClass)

    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it }
    ) {
        AppContent(selectedTab, windowSize)
    }
}

@Composable
fun MainScaffold (
    selectedTab: AppScreen,
    onTabSelected: (AppScreen) -> Unit,
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppNavigationItems(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) {
        content()
    }
}

fun NavigationSuiteScope.AppNavigationItems(
    selectedTab: AppScreen,
    onTabSelected: (AppScreen) -> Unit
) {
    item (
        selected = selectedTab == AppScreen.Home,
        onClick = { onTabSelected(AppScreen.Home) },
        icon = {
            Image (
                painter = painterResource(Res.drawable.iconHouse),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Home") }
    )
    item (
        selected = selectedTab == AppScreen.Training,
        onClick = { onTabSelected(AppScreen.Training) },
        icon = {
            Image (
                painter = painterResource(Res.drawable.iconDumbbell),
                contentDescription = "Training",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Training") }
    )
    item (
        selected = selectedTab == AppScreen.Scanner,
        onClick = { onTabSelected(AppScreen.Scanner) },
        icon = {
            Image (
                painter = painterResource(Res.drawable.iconScanner),
                contentDescription = "Scanner",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Scanner") }
    )
    item (
        selected = selectedTab == AppScreen.Profile,
        onClick = { onTabSelected(AppScreen.Profile) },
        icon = {
            Image (
                painter = painterResource(Res.drawable.iconProfile),
                contentDescription = "Profile",
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text ("Profile") }
    )
}

@Composable
fun AppContent (
    selectedTab: AppScreen,
    windowSize: AppWindowSize
) {
    when (selectedTab) {
        AppScreen.Home -> HomeScreen(windowSize)
        AppScreen.Training -> TrainingScreen()
        AppScreen.Scanner -> ScannerScreen()
        AppScreen.Profile -> ProfileScreen()
    }
}

@Composable
fun TrainingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
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
            Text( "Training screen" )
        }
    }
}

@Composable
fun ScannerScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.error),
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
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.placeholder),
                null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( "Profile screen" )
        }
    }
}

fun getWindowSize(
    windowSizeClass: WindowSizeClass
): AppWindowSize {
    return when {
        windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> AppWindowSize.Extended
        windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> AppWindowSize.Medium
        else -> AppWindowSize.Compact
    }
}

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image (
        painter = painterResource(Res.drawable.logo),
        contentDescription = "App logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun AppName(
    modifier: Modifier = Modifier
) {
    Text (
        text = "RepIt-DoIt",
        style = MaterialTheme.typography.titleLargeEmphasized,
        color = Color.Black,
        modifier = modifier
    )
}