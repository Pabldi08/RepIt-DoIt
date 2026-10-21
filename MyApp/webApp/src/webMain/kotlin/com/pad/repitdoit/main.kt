package com.pad.repitdoit

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.pad.repitdoit.di.DefaultAppContainer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val container = DefaultAppContainer()
    ComposeViewport {
        App(container)
    }
}