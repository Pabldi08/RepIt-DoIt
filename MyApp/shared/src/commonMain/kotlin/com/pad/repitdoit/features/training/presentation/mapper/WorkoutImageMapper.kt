package com.pad.repitdoit.features.training.presentation.mapper

import com.pad.repitdoit.features.training.domain.model.WorkoutCategory
import org.jetbrains.compose.resources.DrawableResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.push
import repitdoit.shared.generated.resources.pull
import repitdoit.shared.generated.resources.upper_body
import repitdoit.shared.generated.resources.lower_body
import repitdoit.shared.generated.resources.full_body
import repitdoit.shared.generated.resources.chest
import repitdoit.shared.generated.resources.back
import repitdoit.shared.generated.resources.shoulders
import repitdoit.shared.generated.resources.arms
import repitdoit.shared.generated.resources.biceps
import repitdoit.shared.generated.resources.triceps
import repitdoit.shared.generated.resources.core
import repitdoit.shared.generated.resources.default

internal fun WorkoutCategory.toDrawable(): DrawableResource =
    when (this) {
        WorkoutCategory.PUSH -> Res.drawable.push
        WorkoutCategory.PULL -> Res.drawable.pull
        WorkoutCategory.UPPER_BODY -> Res.drawable.upper_body
        WorkoutCategory.LOWER_BODY -> Res.drawable.lower_body
        WorkoutCategory.FULL_BODY -> Res.drawable.full_body
        WorkoutCategory.CHEST -> Res.drawable.chest
        WorkoutCategory.BACK -> Res.drawable.back
        WorkoutCategory.SHOULDERS -> Res.drawable.shoulders
        WorkoutCategory.ARMS -> Res.drawable.arms
        WorkoutCategory.BICEPS -> Res.drawable.biceps
        WorkoutCategory.TRICEPS -> Res.drawable.triceps
        WorkoutCategory.CORE -> Res.drawable.core
        WorkoutCategory.DEFAULT -> Res.drawable.default
    }
