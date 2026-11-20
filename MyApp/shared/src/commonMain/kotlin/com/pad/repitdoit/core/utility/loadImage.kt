package com.pad.repitdoit.core.utility

import org.jetbrains.compose.resources.DrawableResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.allDrawableResources
import repitdoit.shared.generated.resources.placeholder

// Busca una imagen por su clave y usa una imagen de reserva cuando la clave no existe.
fun loadImage(image: String?): DrawableResource {
    return image?.let { Res.allDrawableResources[it] } ?: Res.drawable.placeholder
}
