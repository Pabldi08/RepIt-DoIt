package com.pad.repitdoit

import androidx.compose.runtime.Composable
import com.pad.repitdoit.core.ui.theme.AppTheme
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.ui.AppContent

@Composable
fun App(
    appContainer: AppContainer
) {
    AppTheme {
        AppContent(appContainer)
    }
}
