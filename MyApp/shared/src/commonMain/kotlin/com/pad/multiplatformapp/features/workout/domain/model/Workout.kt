package com.pad.multiplatformapp.features.workout.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class Workout (
    val id: Long,
    val name: String,
    val workoutExercises: List<WorkoutExercise> = emptyList(),
    val estimatedTime: Int,
    val image: DrawableResource
)