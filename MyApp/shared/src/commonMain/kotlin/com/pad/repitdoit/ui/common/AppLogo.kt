package com.pad.repitdoit.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image (
        painter = painterResource(Res.drawable.logo),
        contentDescription = "App logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}