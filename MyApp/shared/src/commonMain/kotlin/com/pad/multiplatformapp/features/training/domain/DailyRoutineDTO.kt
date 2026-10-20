package com.pad.multiplatformapp.features.training.domain

data class DailyRoutineDTO (
    val timeBetweenexercises: Float,
    val mucleGroup: List<MuscleGroup>,
    val routineExerciseIdList: List<Int>
)