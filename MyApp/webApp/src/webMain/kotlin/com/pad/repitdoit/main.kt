package com.pad.repitdoit

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.pad.repitdoit.di.DefaultAppContainer

// Monta la aplicación compartida en el navegador.
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val container = DefaultAppContainer()
    ComposeViewport {
        App(container)
    }
}