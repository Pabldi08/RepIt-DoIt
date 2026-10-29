package com.pad.repitdoit.features.training.domain.model

data class WorkoutExercise (
    val id: String,
    val exerciseId: String,
    val name: String,
    val weight: Double,
    val numReps: Int,
    val numSets: Int,
    val restSec: Int
)
