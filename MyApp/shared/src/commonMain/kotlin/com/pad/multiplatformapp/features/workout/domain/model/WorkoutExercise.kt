package com.pad.multiplatformapp.features.workout.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class WorkoutExercise (
    val id: Long,
    val exerciseId: Long,
    val weight: Double,
    val numReps: Int,
    val numSets: Int,
)