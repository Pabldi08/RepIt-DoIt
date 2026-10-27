package com.pad.repitdoit.features.workout.data.repository

import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.model.WorkoutExercise
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_muscles

class FakeWorkoutRepositoryImpl: WorkoutRepository {
    override suspend fun getWorkout(workoutId: String): Workout? {
        return Workout(
            id = workoutId,
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
    }

    override suspend fun createWorkout(name: String): Workout {
        TODO("Not yet implemented")
    }

    override suspend fun renameWorkout(workoutId: String, name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWorkout(workoutId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addWorkoutExercise(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int
    ): WorkoutExercise {
        TODO("Not yet implemented")
    }

    override suspend fun updateWorkoutExercise(workoutId: String, workoutExercise: WorkoutExercise) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWorkoutExercise(workoutId: String, workoutExerciseId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWorkouts(): List<Workout> {
        TODO("Not yet implemented")
    }
}
