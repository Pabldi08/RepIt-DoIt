package com.pad.repitdoit.features.workout.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class Workout (
    val id: String,
    val name: String,
    val workoutExercises: List<WorkoutExercise> = emptyList(),
    val estimatedTime: Int,
    val image: DrawableResource
)