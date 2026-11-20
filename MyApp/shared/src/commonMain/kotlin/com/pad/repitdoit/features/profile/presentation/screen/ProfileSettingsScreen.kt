package com.pad.repitdoit.features.profile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Muestra las opciones de configuración del perfil.
@Composable
internal fun ProfileSettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth()
                .padding(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Ajustes",
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onBack) { Text("Volver") }
            }
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Versión", style = MaterialTheme.typography.titleMedium)
                    Text("1.0", style = MaterialTheme.typography.bodyLarge)
                }
            }
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Política de privacidad y tratamiento de datos", style = MaterialTheme.typography.titleMediumEmphasized)
                    Text(
                        "Usamos la cámara solo para leer los códigos QR de las máquinas y el NFC " +
                            "solo para leer sus etiquetas. El escaneo identifica la máquina y muestra sus ejercicios. " +
                            "La app no guarda fotos ni vídeos."
                    )
                    Text(
                        "RepIt-DoIt no recopila datos personales. " +
                            "Tu perfil, tus planes y tu historial de entrenamientos se guardan en el dispositivo. "
                    )
                    Text(
                        "Puedes retirar el permiso de cámara desde los ajustes del dispositivo " +
                            "y seguir usando las funciones que no requieren escanear un código QR."
                    )
                    Text(
                        "Para recibir notificaciones de la app debes permitir su envío en " +
                                "los ajustes de tu dispositivo."
                    )
                }
            }
        }
    }
}
