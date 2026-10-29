package com.pad.repitdoit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.pad.repitdoit.core.localdatabase.AndroidDatabaseDriverFactory
import com.pad.repitdoit.core.localdatabase.createDatabase
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.camera.QrCameraScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container =
            (application as RepItDoItApplication).appContainer

        setContent {
            App(
                appContainer = container,
                qrCameraScreen = { onClose ->
                    QrCameraScreen(onClose = onClose)
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(
        appContainer = remember {
            DefaultAppContainer()
        }
    )
}
