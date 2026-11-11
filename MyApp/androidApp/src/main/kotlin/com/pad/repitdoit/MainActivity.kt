package com.pad.repitdoit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container =
            (application as RepItDoItApplication).appContainer

        setContent {
            App(
                appContainer = container,
                qrCameraScreen = { QrCameraScreen() }
            )
        }

        if (savedInstanceState == null) {
            showNfcEquipmentId(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        showNfcEquipmentId(intent)
    }

    private fun showNfcEquipmentId(intent: Intent) {
        val equipmentId = intent.readNfcEquipmentId() ?: return
        Toast.makeText(this, "ID leído: $equipmentId", Toast.LENGTH_SHORT).show()
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
