package com.pad.repitdoit.features.workout.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.workout.domain.model.Workout
import com.pad.repitdoit.features.workout.domain.model.WorkoutExercise
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.placeholder
import kotlin.uuid.Uuid


class WorkoutRepositoryImpl (
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : WorkoutRepository {

    override suspend fun getWorkout(
        workoutId: String
    ): Workout? = withContext(dispatcher) {
        val workout = db.workoutQueries
            .selectWorkoutById(
                id = workoutId,
                user_id = "test-user-001"
            )
            .executeAsOneOrNull()
            ?: return@withContext null

        val exercises = db.workoutExerciseQueries
            .selectExercisesByWorkout(workoutId)
            .executeAsList()
            .map { exercise ->
            WorkoutExercise (
                id = exercise.id,
                exerciseId = exercise.exercise_id,
                name = exercise.name,
                weight = exercise.target_weight ?:0.0,
                numReps = exercise.target_reps?.toInt() ?:0,
                numSets = exercise.target_sets?.toInt() ?:0,
                restSec = exercise.rest_seconds?.toInt() ?:0
            )
        }

        Workout (
            id = workout.id,
            name = workout.name,
            workoutExercises = exercises,
            estimatedTime = workout.estimated_time?.toInt() ?:0,
            image = Res.drawable.placeholder
        )
    }

    override suspend fun createWorkout(
        name: String
    ): Workout = withContext(dispatcher){
        // Validar nombre
        // Generar id unico y obtener timestamp

        val id = Uuid.random().toString()
        val timestamp = 261025171100

        db.workoutQueries.insertWorkout(
            id = id,
            user_id = "test-user-001",
            name = name,
            description = "",
            estimated_time = null,
            created_at = timestamp,
            updated_at = timestamp,
        )

        Workout(
            id = id,
            name = name,
            estimatedTime = 0,
            image = Res.drawable.placeholder
        )
    }

    override suspend fun renameWorkout(
        workoutId: String,
        name: String
    ) = withContext(dispatcher){
        //Validar nombre
        // Coger timestamp actual
        val timestamp = 261026171100
        val workout = db.workoutQueries.renameWorkout(
            id = workoutId,
            user_id = "test-user-001",
            name = name,
            updated_at = timestamp
        )
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

    override suspend fun updateWorkoutExercise(
        workoutId: String,
        workoutExercise: WorkoutExercise
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWorkoutExercise(
        workoutId: String,
        workoutExerciseId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWorkouts(): List<Workout> =
        withContext(dispatcher) {
            db.transactionWithResult {
                val workouts = db.workoutQueries
                    .selectAllWorkoutsByUser("test-user-001")
                    .executeAsList()

                val exercisesByWorkout = db.workoutExerciseQueries
                    .selectExercisesByUser("test-user-001")
                    .executeAsList()
                    .groupBy { it.workout_id }

                workouts.map { workout ->
                    val exercises = exercisesByWorkout[workout.id]
                        .orEmpty()
                        .map { exercise ->
                            WorkoutExercise (
                                id = exercise.id,
                                exerciseId = exercise.exercise_id,
                                name = exercise.name,
                                weight = exercise.target_weight?: 0.0,
                                numReps = exercise.target_reps?.toInt() ?: 0,
                                numSets = exercise.target_sets?.toInt() ?: 0,
                                restSec = exercise.rest_seconds?.toInt() ?:0
                            )
                        }

                    Workout (
                        id = workout.id,
                        name = workout.name,
                        workoutExercises = exercises,
                        estimatedTime = workout.estimated_time?.toInt() ?: 0,
                        image = Res.drawable.placeholder
                    )
                }
        }
    }

}
