package com.pad.repitdoit.features.training.domain.model

data class ScheduledWorkout(
    val id: String,
    val workoutId: String,
    val workoutName: String,
    val numEx: Int,
    val weekday: Int,
    val position: Int
)
