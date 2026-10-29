package com.pad.repitdoit.core.utility

import org.jetbrains.compose.resources.DrawableResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.placeholder
import repitdoit.shared.generated.resources.training_legs
import repitdoit.shared.generated.resources.training_muscles

fun loadImage(image: String?): DrawableResource {
    return when(image) {
        "training_muscles" -> Res.drawable.training_muscles
        "training_legs" -> Res.drawable.training_legs
        else -> Res.drawable.placeholder
    }
}