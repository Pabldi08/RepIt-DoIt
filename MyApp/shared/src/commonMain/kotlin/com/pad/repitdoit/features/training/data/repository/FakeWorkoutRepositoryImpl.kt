package com.pad.repitdoit.features.training.data.repository

import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.features.training.domain.model.Exercise
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_muscles

class FakeWorkoutRepositoryImpl: WorkoutRepository {
    private val workouts = mutableMapOf(
        "001" to Workout(
            id = "001",
            name = "Tren Superior",
            estimatedTime = 0,
            image = Res.drawable.training_muscles,
            workoutExercises = listOf(
                WorkoutExercise(
                    id = "1",
                    exerciseId = "10",
                    name = "Press de banca",
                    weight = 40.0,
                    numReps = 10,
                    numSets = 3,
                    restSec = 30
                ),
                WorkoutExercise(
                    id = "2",
                    exerciseId = "20",
                    name = "Remo con mancuerna",
                    weight = 16.0,
                    numReps = 12,
                    numSets = 3,
                    restSec = 30
                ),
                WorkoutExercise(
                    id = "3",
                    exerciseId = "30",
                    name = "Jalón al pecho",
                    weight = 50.0,
                    numReps = 10,
                    numSets = 4,
                    restSec = 30
                ),
                WorkoutExercise(
                    id = "4",
                    exerciseId = "40",
                    name = "Curl de bíceps",
                    weight = 20.0,
                    numReps = 10,
                    numSets = 4,
                    restSec = 30
                ),
            )
        )
    )
    private val archivedWorkoutIds = mutableSetOf<String>()
    private val exercises = workouts.values.flatMap { it.workoutExercises }
        .map { Exercise(it.exerciseId, it.name, null) }.distinctBy { it.id }.sortedBy { it.name }

    override suspend fun getExercises(): List<Exercise> {
        return exercises
    }

    override suspend fun getWorkout(workoutId: String): Workout? {
        return workouts[workoutId]
    }

    override suspend fun createWorkout(name: String): Workout {
        val workout = Workout(generateId(), name, estimatedTime = 0, image = Res.drawable.training_muscles)
        workouts[workout.id] = workout
        return workout
    }

    override suspend fun renameWorkout(workoutId: String, name: String) {
        val workout = requireActiveWorkout(workoutId)
        workouts[workoutId] = workout.copy(name = name)
    }

    override suspend fun removeWorkout(workoutId: String) {
        requireActiveWorkout(workoutId)
        archivedWorkoutIds.add(workoutId)
    }

    override suspend fun addWorkoutExercise(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int,
        weight: Double,
        restSec: Int
    ): WorkoutExercise {
        val workout = requireActiveWorkout(workoutId)
        val exercise = requireNotNull(exercises.find { it.id == exerciseId }) { "El ejercicio no existe o no esta activo" }
        val workoutExercise = WorkoutExercise(generateId(), exerciseId, exercise.name, weight, numReps, numSeries, restSec)
        workouts[workoutId] = workout.copy(workoutExercises = workout.workoutExercises + workoutExercise)
        return workoutExercise
    }

    override suspend fun updateWorkoutExercise(workoutId: String, workoutExercise: WorkoutExercise) {
        val workout = requireActiveWorkout(workoutId)
        val existing = requireNotNull(workout.workoutExercises.find { it.id == workoutExercise.id }) {
            "El ejercicio no pertenece a la rutina"
        }
        require(existing.exerciseId == workoutExercise.exerciseId) { "No se puede cambiar el ejercicio del catalogo" }
        workouts[workoutId] = workout.copy(workoutExercises = workout.workoutExercises.map {
            if (it.id == existing.id) existing.copy(
                weight = workoutExercise.weight,
                numReps = workoutExercise.numReps,
                numSets = workoutExercise.numSets,
                restSec = workoutExercise.restSec
            ) else it
        })
    }

    override suspend fun removeWorkoutExercise(workoutId: String, workoutExerciseId: String) {
        val workout = requireActiveWorkout(workoutId)
        require(workout.workoutExercises.any { it.id == workoutExerciseId }) { "El ejercicio no pertenece a la rutina" }
        workouts[workoutId] = workout.copy(workoutExercises = workout.workoutExercises.filterNot { it.id == workoutExerciseId })
    }

    override suspend fun getAllWorkouts(): List<Workout> {
        return workouts.values.filterNot { it.id in archivedWorkoutIds }
    }

    private fun requireActiveWorkout(workoutId: String): Workout {
        val workout = requireNotNull(workouts[workoutId]) { "La rutina no existe o no pertenece al usuario" }
        require(workoutId !in archivedWorkoutIds) { "La rutina esta archivada" }
        return workout
    }
}
