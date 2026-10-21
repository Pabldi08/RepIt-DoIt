package com.pad.repitdoit

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pad.repitdoit.core.localdatabase.JvmDatabaseDriverFactory
import com.pad.repitdoit.core.localdatabase.createDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.di.DefaultAppContainer
import kotlinx.coroutines.Dispatchers

fun main() {

    val db = createDatabase(JvmDatabaseDriverFactory())

    val container = DefaultAppContainer(
        db = db,
        dispatcher = Dispatchers.IO
    )

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "RepIt-DoIt",
        ) {
            App(container)
        }
    }
}