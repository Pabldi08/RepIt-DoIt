package com.pad.repitdoit.core.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.pad.repitdoit.core.utility.loadImage
import org.jetbrains.compose.resources.painterResource

// Muestra el logotipo con el tamaño y la escala indicados por el componente.
@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image (
        painter = painterResource(loadImage("logo")),
        contentDescription = "App logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}