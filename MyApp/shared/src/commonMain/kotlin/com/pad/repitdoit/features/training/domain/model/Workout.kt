package com.pad.repitdoit.features.training.domain.model

import com.pad.repitdoit.features.training.domain.logic.WorkoutClassifier

// Agrupa la información y los ejercicios de un entrenamiento.
data class Workout(
    val id: String,
    val name: String,
    val workoutExercises: List<WorkoutExercise> = emptyList()
) {
    val completionXp: Long
        get() = workoutExercises.size * 5L

    val category: WorkoutCategory
        get() = WorkoutClassifier.classify(workoutExercises)
}
