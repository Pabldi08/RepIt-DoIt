package com.pad.repitdoit.features.training.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.training.domain.model.Exercise
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.placeholder


class WorkoutRepositoryImpl (
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : WorkoutRepository {

    override suspend fun getExercises(): List<Exercise> = withContext(dispatcher) {
        db.exerciseQueries.selectAllExercises().executeAsList().map { exercise ->
            Exercise(exercise.id, exercise.name, exercise.description)
        }
    }

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
            image = loadImage(workout.image_url)
        )
    }

    override suspend fun createWorkout(
        name: String
    ): Workout = withContext(dispatcher){
        // Validar nombre
        // Generar id unico y obtener timestamp

        val id = generateId()
        val timestamp = 261025171100

        db.workoutQueries.insertWorkout(
            id = id,
            user_id = "test-user-001",
            name = name,
            description = "",
            image_url = null,
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

    override suspend fun removeWorkout(workoutId: String) = withContext(dispatcher) {
        db.transaction {
            val workout = requireNotNull(
                db.workoutQueries.selectWorkoutById(workoutId, "test-user-001").executeAsOneOrNull()
            ) { "La rutina no existe o no pertenece al usuario" }
            require(workout.archived_at == null) { "La rutina esta archivada" }
            val timestamp = maxOf(Clock.System.now().toEpochMilliseconds(), workout.updated_at)
            db.workoutQueries.archiveWorkout(timestamp, timestamp, workoutId, "test-user-001")
        }
    }

    override suspend fun addWorkoutExercise(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int,
        weight: Double,
        restSec: Int
    ): WorkoutExercise = withContext(dispatcher) {
        db.transactionWithResult {
            val workout = requireNotNull(
                db.workoutQueries.selectWorkoutById(workoutId, "test-user-001").executeAsOneOrNull()
            ) { "La rutina no existe o no pertenece al usuario" }
            require(workout.archived_at == null) { "La rutina esta archivada" }
            val exercise = requireNotNull(
                db.exerciseQueries.selectExerciseById(exerciseId).executeAsOneOrNull()
            ) { "El ejercicio no existe" }
            require(exercise.is_active == 1L) { "El ejercicio no esta activo" }
            val position = db.workoutExerciseQueries.selectExercisesByWorkout(workoutId)
                .executeAsList().maxOfOrNull { it.position }?.plus(1) ?: 0L
            val id = generateId()
            db.workoutExerciseQueries.insertWorkoutExercise(
                id, workoutId, exerciseId, position, weight,
                numSeries.toLong(), numReps.toLong(), restSec.toLong(), null
            )
            WorkoutExercise(id, exerciseId, exercise.name, weight, numReps, numSeries, restSec)
        }
    }

    override suspend fun updateWorkoutExercise(
        workoutId: String,
        workoutExercise: WorkoutExercise
    ) = withContext(dispatcher) {
        db.transaction {
            val workout = requireNotNull(
                db.workoutQueries.selectWorkoutById(workoutId, "test-user-001").executeAsOneOrNull()
            ) { "La rutina no existe o no pertenece al usuario" }
            require(workout.archived_at == null) { "La rutina esta archivada" }
            val existing = requireNotNull(
                db.workoutExerciseQueries.selectWorkoutExerciseById(workoutExercise.id).executeAsOneOrNull()
            ) { "La entrada del ejercicio no existe" }
            require(existing.workout_id == workoutId) { "El ejercicio no pertenece a la rutina" }
            require(existing.exercise_id == workoutExercise.exerciseId) { "No se puede cambiar el ejercicio del catalogo" }
            db.workoutExerciseQueries.updateWorkoutExercise(
                workoutExercise.weight, workoutExercise.numSets.toLong(),
                workoutExercise.numReps.toLong(), workoutExercise.restSec.toLong(),
                existing.notes, existing.id
            )
        }
    }

    override suspend fun removeWorkoutExercise(
        workoutId: String,
        workoutExerciseId: String
    ) = withContext(dispatcher) {
        db.transaction {
            val workout = requireNotNull(
                db.workoutQueries.selectWorkoutById(workoutId, "test-user-001").executeAsOneOrNull()
            ) { "La rutina no existe o no pertenece al usuario" }
            require(workout.archived_at == null) { "La rutina esta archivada" }
            val existing = requireNotNull(
                db.workoutExerciseQueries.selectWorkoutExerciseById(workoutExerciseId).executeAsOneOrNull()
            ) { "La entrada del ejercicio no existe" }
            require(existing.workout_id == workoutId) { "El ejercicio no pertenece a la rutina" }
            db.workoutExerciseQueries.deleteWorkoutExercise(workoutExerciseId)
        }
    }

    override suspend fun getAllWorkouts(): List<Workout> =
        withContext(dispatcher) {
            db.transactionWithResult {
                val workouts = db.workoutQueries
                    .selectWorkoutsByUser("test-user-001")
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
                        image = loadImage(workout.image_url)
                    )
                }
        }
    }

}
