package com.pad.repitdoit.features.training.domain.model

import com.pad.repitdoit.features.training.domain.logic.WorkoutClassifier

data class Workout(
    val id: String,
    val name: String,
    val workoutExercises: List<WorkoutExercise> = emptyList()
) {
    val category: WorkoutCategory
        get() = WorkoutClassifier.classify(workoutExercises)
}
