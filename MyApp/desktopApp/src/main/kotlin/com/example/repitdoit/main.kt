package com.example.repitdoit

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pad.multiplatformapp.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RepIt-DoIt",
    ) {
        App()
    }
}