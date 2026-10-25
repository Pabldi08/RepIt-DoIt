package com.pad.repitdoit.features.workout.domain.model

data class WorkoutExercise (
    val id: String,
    val exerciseId: String,
    val weight: Double,
    val numReps: Int,
    val numSets: Int,
    val restSec: Int
)