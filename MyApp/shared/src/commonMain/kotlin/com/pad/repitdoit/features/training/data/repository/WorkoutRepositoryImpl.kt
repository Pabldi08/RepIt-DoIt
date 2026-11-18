
package com.pad.repitdoit.features.training.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
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

class WorkoutRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val currentUserId: () -> String?
) : WorkoutRepository {

    private fun requireUserId(): String =
        checkNotNull(currentUserId()) {
            "No hay ninguna sesión activa"
        }

    private fun requirePlan(
        planId: String,
        userId: String
    ) = requireNotNull(
        db.trainingPlanQueries
            .selectPlanById(planId, userId)
            .executeAsOneOrNull()
    ) {
        "El plan no existe o no pertenece al usuario"
    }

    private fun requireSchedule(
        planId: String,
        scheduleId: String,
        userId: String
    ) = requireNotNull(
        db.trainingPlanQueries
            .selectPlanScheduleEntry(scheduleId, planId, userId)
            .executeAsOneOrNull()
    ) {
        "La rutina programada no pertenece a este plan"
    }

    override suspend fun selectTrainingPlan(
        planId: String
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val plan = requirePlan(planId, userId)

            val activePlan = db.trainingPlanQueries
                .selectActivePlan(userId)
                .executeAsOneOrNull()

            val timestamp = maxOf(
                Clock.System.now().toEpochMilliseconds(),
                plan.updated_at,
                activePlan?.updated_at ?: 0L
            )

            db.trainingPlanQueries.deactivateUserPlans(
                timestamp,
                userId
            )

            db.trainingPlanQueries.activateTrainingPlan(
                timestamp,
                planId,
                userId
            )
        }
    }

    override suspend fun createTrainingPlan(
        name: String
    ): TrainingPlan = withContext(dispatcher) {

        val userId = requireUserId()

        val id = generateId()
        val timestamp = Clock.System.now().toEpochMilliseconds()

        db.trainingPlanQueries.insertTrainingPlan(
            id,
            userId,
            name,
            timestamp,
            timestamp
        )

        TrainingPlan(
            id = id,
            name = name,
            isActive = false
        )
    }

    override suspend fun renameTrainingPlan(
        planId: String,
        name: String
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val plan = requirePlan(planId, userId)

            val timestamp = maxOf(
                Clock.System.now().toEpochMilliseconds(),
                plan.updated_at
            )

            db.trainingPlanQueries.updateTrainingPlan(
                name,
                timestamp,
                planId,
                userId
            )
        }
    }

    override suspend fun deleteTrainingPlan(
        planId: String
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            requirePlan(planId, userId)

            db.trainingPlanQueries.deleteTrainingPlan(
                planId,
                userId
            )
        }
    }

    override suspend fun addWorkoutToPlan(
        planId: String,
        workoutId: String,
        weekday: Int
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val plan = requirePlan(planId, userId)

            val workout = requireNotNull(
                db.workoutQueries
                    .selectWorkoutById(workoutId, userId)
                    .executeAsOneOrNull()
            ) {
                "La rutina no existe o no pertenece al usuario"
            }

            require(workout.archived_at == null) {
                "La rutina está archivada"
            }

            val scheduleId = generateId()

            db.trainingPlanQueries.addWorkoutToPlan(
                schedule_id = scheduleId,
                weekday = weekday.toLong(),
                plan_id = planId,
                user_id = userId,
                workout_id = workoutId
            )

            requireSchedule(planId, scheduleId, userId)

            db.trainingPlanQueries.touchTrainingPlan(
                updated_at = maxOf(
                    Clock.System.now().toEpochMilliseconds(),
                    plan.updated_at
                ),
                plan_id = planId,
                user_id = userId
            )
        }
    }

    override suspend fun removeWorkoutFromPlan(
        planId: String,
        scheduleId: String
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val plan = requirePlan(planId, userId)

            requireSchedule(planId, scheduleId, userId)

            db.trainingPlanQueries.removeWorkoutFromPlan(
                scheduleId,
                planId,
                userId
            )

            db.trainingPlanQueries.touchTrainingPlan(
                updated_at = maxOf(
                    Clock.System.now().toEpochMilliseconds(),
                    plan.updated_at
                ),
                plan_id = planId,
                user_id = userId
            )
        }
    }

    override suspend fun moveWorkoutToPlanDay(
        planId: String,
        scheduleId: String,
        weekday: Int
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val plan = requirePlan(planId, userId)

            val entry = requireSchedule(
                planId,
                scheduleId,
                userId
            )

            if (entry.weekday != weekday.toLong()) {

                db.trainingPlanQueries.moveWorkoutToPlanDay(
                    weekday = weekday.toLong(),
                    plan_id = planId,
                    user_id = userId,
                    schedule_id = scheduleId
                )

                db.trainingPlanQueries.touchTrainingPlan(
                    updated_at = maxOf(
                        Clock.System.now().toEpochMilliseconds(),
                        plan.updated_at
                    ),
                    plan_id = planId,
                    user_id = userId
                )
            }
        }
    }

    override suspend fun getTrainingPlans(): List<TrainingPlan> =
        withContext(dispatcher) {

            val userId = currentUserId()
                ?: return@withContext emptyList()

            db.transactionWithResult {

                db.trainingPlanQueries
                    .selectPlansByUser(userId)
                    .executeAsList()
                    .map { plan ->

                        val schedule = db.trainingPlanQueries
                            .selectPlanSchedule(
                                plan_id = plan.id,
                                user_id = userId
                            )
                            .executeAsList()
                            .map { entry ->

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

        val exercise = db.exerciseQueries
            .selectExerciseById(exerciseId)
            .executeAsOneOrNull()
            ?: return@withContext null

        if (exercise.is_active != 1L) {
            return@withContext null
        }

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

        val userId = currentUserId()
            ?: return@withContext null

        val workout = db.workoutQueries
            .selectWorkoutById(
                id = workoutId,
                user_id = userId
            )
            .executeAsOneOrNull()
            ?: return@withContext null

        val exercises = db.workoutExerciseQueries
            .selectExercisesByWorkout(workoutId)
            .executeAsList()
            .map { exercise ->

                WorkoutExercise(
                    id = exercise.id,
                    exerciseId = exercise.exercise_id,
                    name = exercise.name,
                    weight = exercise.target_weight ?: 0.0,
                    numReps = exercise.target_reps?.toInt() ?: 0,
                    numSets = exercise.target_sets?.toInt() ?: 0,
                    restSec = exercise.rest_seconds?.toInt() ?: 0
                )
            }

        Workout(
            id = workout.id,
            name = workout.name,
            workoutExercises = exercises,
            image = loadImage(workout.image_url)
        )
    }

    override suspend fun createWorkout(
        name: String
    ): Workout = withContext(dispatcher) {

        val userId = requireUserId()

        val id = generateId()
        val timestamp = Clock.System.now().toEpochMilliseconds()

        db.workoutQueries.insertWorkout(
            id = id,
            user_id = userId,
            name = name,
            description = "",
            image_url = null,
            estimated_time = null,
            created_at = timestamp,
            updated_at = timestamp
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
    ): Unit = withContext(dispatcher) {

        val userId = requireUserId()

        val timestamp = Clock.System.now().toEpochMilliseconds()

        db.workoutQueries.renameWorkout(
            id = workoutId,
            user_id = userId,
            name = name,
            updated_at = timestamp
        )

        Unit
    }

    override suspend fun removeWorkout(
        workoutId: String
    ) = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val workout = requireNotNull(
                db.workoutQueries
                    .selectWorkoutById(workoutId, userId)
                    .executeAsOneOrNull()
            ) {
                "La rutina no existe o no pertenece al usuario"
            }

            require(workout.archived_at == null) {
                "La rutina está archivada"
            }

            val timestamp = maxOf(
                Clock.System.now().toEpochMilliseconds(),
                workout.updated_at
            )

            db.workoutQueries.archiveWorkout(
                timestamp,
                timestamp,
                workoutId,
                userId
            )
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

        val userId = requireUserId()

        db.transactionWithResult {

            val workout = requireNotNull(
                db.workoutQueries
                    .selectWorkoutById(workoutId, userId)
                    .executeAsOneOrNull()
            ) {
                "La rutina no existe o no pertenece al usuario"
            }

            require(workout.archived_at == null) {
                "La rutina está archivada"
            }

            val exercise = requireNotNull(
                db.exerciseQueries
                    .selectExerciseById(exerciseId)
                    .executeAsOneOrNull()
            ) {
                "El ejercicio no existe"
            }

            require(exercise.is_active == 1L) {
                "El ejercicio no está activo"
            }

            val position = db.workoutExerciseQueries
                .selectExercisesByWorkout(workoutId)
                .executeAsList()
                .maxOfOrNull { it.position }
                ?.plus(1) ?: 0L

            val id = generateId()

            db.workoutExerciseQueries.insertWorkoutExercise(
                id,
                workoutId,
                exerciseId,
                position,
                weight,
                numSeries.toLong(),
                numReps.toLong(),
                restSec.toLong(),
                null
            )

            WorkoutExercise(
                id,
                exerciseId,
                exercise.name,
                weight,
                numReps,
                numSeries,
                restSec
            )
        }
    }

    override suspend fun updateWorkoutExercise(
        workoutId: String,
        workoutExercise: WorkoutExercise
    ) = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val workout = requireNotNull(
                db.workoutQueries
                    .selectWorkoutById(workoutId, userId)
                    .executeAsOneOrNull()
            ) {
                "La rutina no existe o no pertenece al usuario"
            }

            require(workout.archived_at == null) {
                "La rutina está archivada"
            }

            val existing = requireNotNull(
                db.workoutExerciseQueries
                    .selectWorkoutExerciseById(workoutExercise.id)
                    .executeAsOneOrNull()
            ) {
                "La entrada del ejercicio no existe"
            }

            require(existing.workout_id == workoutId) {
                "El ejercicio no pertenece a la rutina"
            }

            require(existing.exercise_id == workoutExercise.exerciseId) {
                "No se puede cambiar el ejercicio del catálogo"
            }

            db.workoutExerciseQueries.updateWorkoutExercise(
                workoutExercise.weight,
                workoutExercise.numSets.toLong(),
                workoutExercise.numReps.toLong(),
                workoutExercise.restSec.toLong(),
                existing.notes,
                existing.id
            )
        }
    }

    override suspend fun removeWorkoutExercise(
        workoutId: String,
        workoutExerciseId: String
    ) = withContext(dispatcher) {

        val userId = requireUserId()

        db.transaction {

            val workout = requireNotNull(
                db.workoutQueries
                    .selectWorkoutById(workoutId, userId)
                    .executeAsOneOrNull()
            ) {
                "La rutina no existe o no pertenece al usuario"
            }

            require(workout.archived_at == null) {
                "La rutina está archivada"
            }

            val existing = requireNotNull(
                db.workoutExerciseQueries
                    .selectWorkoutExerciseById(workoutExerciseId)
                    .executeAsOneOrNull()
            ) {
                "La entrada del ejercicio no existe"
            }

            require(existing.workout_id == workoutId) {
                "El ejercicio no pertenece a la rutina"
            }

            db.workoutExerciseQueries.deleteWorkoutExercise(
                workoutExerciseId
            )
        }
    }

    override suspend fun getAllWorkouts(): List<Workout> =
        withContext(dispatcher) {

            val userId = currentUserId()
                ?: return@withContext emptyList()

            db.transactionWithResult {

                val workouts = db.workoutQueries
                    .selectWorkoutsByUser(userId)
                    .executeAsList()

                val exercisesByWorkout = db.workoutExerciseQueries
                    .selectExercisesByUser(userId)
                    .executeAsList()
                    .groupBy { it.workout_id }

                workouts.map { workout ->

                    val exercises = exercisesByWorkout[workout.id]
                        .orEmpty()
                        .map { exercise ->

                            WorkoutExercise(
                                id = exercise.id,
                                exerciseId = exercise.exercise_id,
                                name = exercise.name,
                                weight = exercise.target_weight ?: 0.0,
                                numReps = exercise.target_reps?.toInt() ?: 0,
                                numSets = exercise.target_sets?.toInt() ?: 0,
                                restSec = exercise.rest_seconds?.toInt() ?: 0
                            )
                        }

                    Workout(
                        id = workout.id,
                        name = workout.name,
                        workoutExercises = exercises,
                        image = loadImage(workout.image_url)
                    )
                }
            }
        }
}
