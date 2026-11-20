package com.pad.repitdoit.features.session.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pad.repitdoit.core.ui.common.AppLogo
import com.pad.repitdoit.core.ui.common.AppName

// Reutiliza la cabecera de las pantallas de autenticación.
@Composable
fun AuthHeader(subtitle: String = "", isWide: Boolean) {
    if(isWide){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppLogo(
                modifier = Modifier
                    .size(400.dp)
                    .offset(y = 80.dp)
            )

            AppName(fontSize = 40.sp)

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    else{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppLogo(
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = 24.dp)
            )

            AppName(fontSize = 40.sp)

            if(subtitle != ""){
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}