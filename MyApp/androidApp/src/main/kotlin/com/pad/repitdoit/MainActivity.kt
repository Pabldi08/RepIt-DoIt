package com.pad.repitdoit

import android.content.Intent
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
import com.pad.repitdoit.nfc.readNfcEquipmentId
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class MainActivity : ComponentActivity() {
    private val nfcScans = Channel<String>(Channel.BUFFERED)
    private val nfcEquipmentIds = nfcScans.receiveAsFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container =
            (application as RepItDoItApplication).appContainer

        setContent {
            App(
                appContainer = container,
                qrCameraScreen = { onEquipmentId -> QrCameraScreen(onEquipmentId) },
                nfcEquipmentIds = nfcEquipmentIds
            )
        }

        if (savedInstanceState == null) {
            handleNfcIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        val equipmentId = intent.readNfcEquipmentId() ?: return
        nfcScans.trySend(equipmentId)
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
