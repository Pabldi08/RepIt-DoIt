package com.pad.repitdoit.features.workout.domain.model

data class WorkoutSchedule(
    val id: String,
    val userId: String,
    val trainingPlanId: String,
    val workoutId: String,
    val weekday: Int,
    val position: Int
)