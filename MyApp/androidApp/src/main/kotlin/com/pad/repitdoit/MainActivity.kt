package com.pad.repitdoit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.pad.repitdoit.core.localdatabase.AndroidDatabaseDriverFactory
import com.pad.repitdoit.core.localdatabase.createDatabase
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.camera.QrCameraScreen
import com.pad.repitdoit.navigation.Route
import com.pad.repitdoit.nfc.readNfcEquipmentId
import com.pad.repitdoit.notifications.EXTRA_TO_SCREEN
import com.pad.repitdoit.notifications.Notification
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private val nfcScans = Channel<String>(Channel.BUFFERED)
    private val nfcEquipmentIds = nfcScans.receiveAsFlow()
    private var toScreen by mutableStateOf<Route?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container =
            (application as RepItDoItApplication).appContainer

        setContent {
            App(
                appContainer = container,
                qrCameraScreen = { onEquipmentId -> QrCameraScreen(onEquipmentId) },
                nfcEquipmentIds = nfcEquipmentIds,
                toScreen = toScreen
            )
        }

        if (savedInstanceState == null) {
            toScreen = jsonToRoute(intent)
            handleNfcIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        toScreen = jsonToRoute(intent)
        handleNfcIntent(intent)
    }

    override fun onStop() {
        super.onStop()
        Notification.scheduleNotification(
            context = applicationContext,
            title = "RepItDoIt",
            message = "¡Hora de entrenar!",
            delaySeconds = 5,
            toScreen = Route.Training
        )
    }

    private fun jsonToRoute(intent: Intent?): Route? {
        val json = intent?.getStringExtra(EXTRA_TO_SCREEN) ?: return null
        return runCatching { Json.decodeFromString(Route.serializer(), json) }.getOrNull()
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
