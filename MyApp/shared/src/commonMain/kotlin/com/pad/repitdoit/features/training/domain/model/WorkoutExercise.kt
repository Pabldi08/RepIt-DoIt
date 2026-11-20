package com.pad.repitdoit.features.training.domain.model

// Representa un ejercicio de la rutina junto con sus series, repeticiones y peso.
data class WorkoutExercise (
    val id: String,
    val exerciseId: String,
    val name: String,
    val weight: Double,
    val numReps: Int,
    val numSets: Int,
    val restSec: Int,
    val muscleGroups: Set<MuscleGroup> = emptySet()
)
