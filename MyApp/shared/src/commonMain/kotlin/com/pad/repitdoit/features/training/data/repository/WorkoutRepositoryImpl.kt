package com.pad.repitdoit.features.training.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.model.ScheduledWorkout
import com.pad.repitdoit.features.training.domain.model.ExerciseContext
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

    private fun requirePlan(planId: String) = requireNotNull(
        db.trainingPlanQueries.selectPlanById(planId, LOCAL_TEST_USER_ID).executeAsOneOrNull()
    ) { "El plan no existe o no pertenece al usuario" }

    override suspend fun selectTrainingPlan(planId: String): Unit = withContext(dispatcher) {
        db.transaction {
            // Validar antes de desactivar para conservar el anterior si el destino no existe.
            val plan = requirePlan(planId)
            val activePlan = db.trainingPlanQueries.selectActivePlan(LOCAL_TEST_USER_ID)
                .executeAsOneOrNull()
            val timestamp = maxOf(
                Clock.System.now().toEpochMilliseconds(),
                plan.updated_at,
                activePlan?.updated_at ?: 0L
            )
            db.trainingPlanQueries.deactivateUserPlans(timestamp, LOCAL_TEST_USER_ID)
            db.trainingPlanQueries.activateTrainingPlan(timestamp, planId, LOCAL_TEST_USER_ID)
        }
    }

    private fun requireSchedule(planId: String, scheduleId: String) = requireNotNull(
        db.trainingPlanQueries.selectPlanScheduleEntry(scheduleId, planId, LOCAL_TEST_USER_ID)
            .executeAsOneOrNull()
    ) { "La rutina programada no pertenece a este plan" }

    override suspend fun createTrainingPlan(name: String): TrainingPlan = withContext(dispatcher) {
        val id = generateId()
        val timestamp = Clock.System.now().toEpochMilliseconds()
        db.trainingPlanQueries.insertTrainingPlan(id, LOCAL_TEST_USER_ID, name, timestamp, timestamp)
        TrainingPlan(id = id, name = name, isActive = false)
    }

    override suspend fun renameTrainingPlan(planId: String, name: String): Unit = withContext(dispatcher) {
        db.transaction {
            val plan = requirePlan(planId)
            val timestamp = maxOf(Clock.System.now().toEpochMilliseconds(), plan.updated_at)
            db.trainingPlanQueries.updateTrainingPlan(name, timestamp, planId, LOCAL_TEST_USER_ID)
        }
    }

    override suspend fun deleteTrainingPlan(planId: String): Unit = withContext(dispatcher) {
        db.transaction {
            requirePlan(planId)
            db.trainingPlanQueries.deleteTrainingPlan(planId, LOCAL_TEST_USER_ID)
        }
    }

    override suspend fun addWorkoutToPlan(planId: String, workoutId: String, weekday: Int): Unit =
        withContext(dispatcher) {
            db.transaction {
                val plan = requirePlan(planId)
                val workout = requireNotNull(
                    db.workoutQueries.selectWorkoutById(workoutId, LOCAL_TEST_USER_ID).executeAsOneOrNull()
                ) { "La rutina no existe o no pertenece al usuario" }
                require(workout.archived_at == null) { "La rutina está archivada" }
                val scheduleId = generateId()
                db.trainingPlanQueries.addWorkoutToPlan(
                    schedule_id = scheduleId,
                    weekday = weekday.toLong(),
                    plan_id = planId,
                    user_id = LOCAL_TEST_USER_ID,
                    workout_id = workoutId
                )
                requireSchedule(planId, scheduleId)
                db.trainingPlanQueries.touchTrainingPlan(
                    updated_at = maxOf(Clock.System.now().toEpochMilliseconds(), plan.updated_at),
                    plan_id = planId,
                    user_id = LOCAL_TEST_USER_ID
                )
            }
        }

    override suspend fun removeWorkoutFromPlan(planId: String, scheduleId: String): Unit =
        withContext(dispatcher) {
            db.transaction {
                val plan = requirePlan(planId)
                requireSchedule(planId, scheduleId)
                db.trainingPlanQueries.removeWorkoutFromPlan(scheduleId, planId, LOCAL_TEST_USER_ID)
                db.trainingPlanQueries.touchTrainingPlan(
                    updated_at = maxOf(Clock.System.now().toEpochMilliseconds(), plan.updated_at),
                    plan_id = planId,
                    user_id = LOCAL_TEST_USER_ID
                )
            }
        }

    override suspend fun moveWorkoutToPlanDay(planId: String, scheduleId: String, weekday: Int): Unit =
        withContext(dispatcher) {
            db.transaction {
                val plan = requirePlan(planId)
                val entry = requireSchedule(planId, scheduleId)
                if (entry.weekday != weekday.toLong()) {
                    db.trainingPlanQueries.moveWorkoutToPlanDay(
                        weekday = weekday.toLong(),
                        plan_id = planId,
                        user_id = LOCAL_TEST_USER_ID,
                        schedule_id = scheduleId
                    )
                    db.trainingPlanQueries.touchTrainingPlan(
                        updated_at = maxOf(Clock.System.now().toEpochMilliseconds(), plan.updated_at),
                        plan_id = planId,
                        user_id = LOCAL_TEST_USER_ID
                    )
                }
            }
        }

    override suspend fun getTrainingPlans(): List<TrainingPlan> = withContext(dispatcher) {
        db.transactionWithResult {
            db.trainingPlanQueries.selectPlansByUser(LOCAL_TEST_USER_ID)
                .executeAsList().map { plan ->
                    val schedule = db.trainingPlanQueries.selectPlanSchedule(
                        plan_id = plan.id,
                        user_id = LOCAL_TEST_USER_ID
                    ).executeAsList().map { entry ->
                        ScheduledWorkout(
                            id = entry.schedule_id,
                            workoutId = entry.workout_id,
                            workoutName = entry.workout_name,
                            numEx = entry.num_ex.toInt(),
                            weekday = entry.weekday.toInt(),
                            position = entry.position.toInt()
                        )
                    }
                    TrainingPlan(
                        id = plan.id,
                        name = plan.name,
                        isActive = plan.is_active == 1L,
                        schedule = schedule
                    )
                }
        }
    }

    override suspend fun getExerciseContext(
        exerciseId: String
    ): ExerciseContext? = withContext(dispatcher) {
            val exercise = db.exerciseQueries.selectExerciseById(exerciseId)
                .executeAsOneOrNull() ?: return@withContext null
            if (exercise.is_active != 1L) return@withContext null
            ExerciseContext(
                id = exercise.id,
                name = exercise.name,
                description = exercise.description,
                imageUrl = exercise.image_url
            )
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
            image = loadImage(workout.image_url)
        )
    }

    override suspend fun createWorkout(
        name: String
    ): Workout = withContext(dispatcher){
        // Validar nombre
        // Generar id unico y obtener timestamp

        val id = generateId()
        val time = Clock.System.now()
        val timestamp = time.toEpochMilliseconds()

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
            image = Res.drawable.placeholder
        )
    }

    override suspend fun renameWorkout(
        workoutId: String,
        name: String
    ) = withContext(dispatcher){
        //Validar nombre
        // Coger timestamp actual
        val time = Clock.System.now()
        val timestamp = time.toEpochMilliseconds()
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
                        image = loadImage(workout.image_url)
                    )
                }
        }
    }

}
