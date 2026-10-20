package com.pad.multiplatformapp.features.training.domain

data class RoutineExerciseDTO(
    val base: ExerciseDTO,
    val secsInBetween : Int,
    val weight : Int
)