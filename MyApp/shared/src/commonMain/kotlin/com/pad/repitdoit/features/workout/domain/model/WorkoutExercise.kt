package com.pad.repitdoit.features.workout.domain.model

data class WorkoutExercise(
    val id: String,
    val workoutId: String,
    val exerciseId: String,
    val position: Int,
    val targetSets: Int,
    val targetReps: Int? = null,
    val targetWeight: Double? = null,
    val restSeconds: Int? = null,
    val notes: String? = null
)