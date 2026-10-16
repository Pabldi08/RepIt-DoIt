package com.pad.multiplatformapp.features.workout.data.repository

import com.pad.multiplatformapp.features.workout.domain.model.Workout
import com.pad.multiplatformapp.features.workout.domain.model.WorkoutExercise
import com.pad.multiplatformapp.features.workout.domain.repository.WorkoutRepository
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_muscles

class FakeWorkoutRepositoryImpl: WorkoutRepository {
    override suspend fun getWorkout(workoutId: Long): Workout? {
        return Workout(
            id = workoutId,
            name = "Tren Superior",
            estimatedTime = 0,
            image = Res.drawable.training_muscles,
            workoutExercises = listOf(
                WorkoutExercise(
                    id = 1,
                    exerciseId = 10,
                    weight = 40.0,
                    numReps = 10,
                    numSets = 3
                ),
                WorkoutExercise(
                    id = 2,
                    exerciseId = 20,
                    weight = 16.0,
                    numReps = 12,
                    numSets = 3
                ),
                WorkoutExercise(
                    id = 3,
                    exerciseId = 30,
                    weight = 50.0,
                    numReps = 10,
                    numSets = 4
                ),
                WorkoutExercise(
                    id = 4,
                    exerciseId = 40,
                    weight = 20.0,
                    numReps = 10,
                    numSets = 4
                ),
            )
        )
    }

    override suspend fun createWorkout(name: String): Workout {
        TODO("Not yet implemented")
    }

    override suspend fun renameWorkout(workoutId: Long, name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWorkout(workoutId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun addWorkoutExercise(
        workoutId: Long,
        exerciseId: Long,
        numReps: Int,
        numSeries: Int
    ): WorkoutExercise {
        TODO("Not yet implemented")
    }

    override suspend fun updateWorkoutExercise(workoutId: Long, workoutExercise: WorkoutExercise) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWorkoutExercise(workoutId: Long, workoutExerciseId: Long) {
        TODO("Not yet implemented")
    }
}