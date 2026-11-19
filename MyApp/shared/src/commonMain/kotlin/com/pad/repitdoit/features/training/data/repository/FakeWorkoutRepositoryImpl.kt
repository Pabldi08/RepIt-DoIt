package com.pad.repitdoit.features.training.data.repository

import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.features.training.domain.model.ExerciseContext
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.TrainingPlan
import com.pad.repitdoit.features.training.domain.model.ScheduledWorkout
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_muscles

class FakeWorkoutRepositoryImpl(
    private val currentUserId: () -> String? = { "fake-local" }
) : WorkoutRepository {

    override suspend fun completeWorkoutSession(
        sessionId: String,
        workout: Workout,
        startedAt: Long,
        finishedAt: Long
    ) = Unit

    private val seedWorkouts = mapOf(
        "001" to Workout(
            id = "001",
            name = "Tren Superior",
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
                )
            )
        )
    )

    private val seedPlans = mapOf(
        "fake-plan-001" to TrainingPlan(
            id = "fake-plan-001",
            name = "Plan semanal de prueba",
            isActive = false,
            schedule = seedWorkouts["001"]?.let { workout ->
                listOf(1, 3, 5).map { day ->
                    ScheduledWorkout(
                        id = "fake-schedule-$day",
                        workoutId = workout.id,
                        workoutName = workout.name,
                        numEx = workout.workoutExercises.size,
                        weekday = day,
                        position = 0
                    )
                }
            }.orEmpty()
        )
    )

    // Datos independientes de cada usuario

    private class UserData(
        val workouts: MutableMap<String, Workout>,
        val plans: MutableMap<String, TrainingPlan>,
        val archivedWorkoutIds: MutableSet<String> = mutableSetOf()
    )

    private val dataByUser = mutableMapOf<String, UserData>()

    private fun getUserData(): UserData {

        val userId = checkNotNull(currentUserId()) {
            "No hay ninguna sesión activa"
        }

        return dataByUser.getOrPut(userId) {
            UserData(
                workouts = seedWorkouts.toMutableMap(),
                plans = seedPlans.toMutableMap()
            )
        }
    }

    private val workouts
        get() = getUserData().workouts

    private val plans
        get() = getUserData().plans

    private val archivedWorkoutIds
        get() = getUserData().archivedWorkoutIds

    private val exercises = seedWorkouts.values
        .flatMap { it.workoutExercises }
        .map {
            ExerciseContext(
                it.exerciseId,
                it.name,
                null,
                null
            )
        }
        .distinctBy { it.id }
        .sortedBy { it.name }

    // Gestión de planes de entrenamiento

    override suspend fun getTrainingPlans(): List<TrainingPlan> =
        plans.values
            .sortedWith(
                compareBy(
                    { it.name.lowercase() },
                    { it.name },
                    { it.id }
                )
            )
            .map { plan ->

                plan.copy(
                    schedule = plan.schedule.mapNotNull { entry ->

                        val workout = workouts[entry.workoutId]
                            ?.takeUnless { it.id in archivedWorkoutIds }
                            ?: return@mapNotNull null

                        entry.copy(
                            workoutName = workout.name,
                            numEx = workout.workoutExercises.size
                        )

                    }.sortedWith(
                        compareBy(
                            { it.weekday },
                            { it.position }
                        )
                    )
                )
            }

    private fun requirePlan(planId: String): TrainingPlan =
        requireNotNull(plans[planId]) {
            "El plan no existe o no pertenece al usuario"
        }

    override suspend fun selectTrainingPlan(planId: String) {

        requirePlan(planId)

        plans.keys.toList().forEach { id ->
            plans[id] = plans.getValue(id).copy(
                isActive = id == planId
            )
        }
    }

    override suspend fun createTrainingPlan(
        name: String
    ): TrainingPlan {

        val plan = TrainingPlan(
            generateId(),
            name,
            false
        )

        plans[plan.id] = plan

        return plan
    }

    override suspend fun renameTrainingPlan(
        planId: String,
        name: String
    ) {
        plans[planId] = requirePlan(planId).copy(
            name = name
        )
    }

    override suspend fun deleteTrainingPlan(planId: String) {

        requirePlan(planId)

        plans.remove(planId)
    }

    override suspend fun addWorkoutToPlan(
        planId: String,
        workoutId: String,
        weekday: Int
    ) {

        val plan = requirePlan(planId)
        val workout = requireActiveWorkout(workoutId)

        val position = plan.schedule
            .filter { it.weekday == weekday }
            .maxOfOrNull { it.position }
            ?.plus(1) ?: 0

        val entry = ScheduledWorkout(
            generateId(),
            workout.id,
            workout.name,
            workout.workoutExercises.size,
            weekday,
            position
        )

        plans[planId] = plan.copy(
            schedule = plan.schedule + entry
        )
    }

    override suspend fun removeWorkoutFromPlan(
        planId: String,
        scheduleId: String
    ) {

        val plan = requirePlan(planId)
        require(plan.schedule.any { it.id == scheduleId }) { "La rutina programada no pertenece a este plan" }
        plans[planId] = plan.copy(schedule = plan.schedule.filterNot { it.id == scheduleId })
    }

    override suspend fun moveWorkoutToPlanDay(
        planId: String,
        scheduleId: String,
        weekday: Int
    ) {

        val plan = requirePlan(planId)
        val entry = requireNotNull(plan.schedule.find { it.id == scheduleId }) {
            "La rutina programada no pertenece a este plan"
        }

        if (entry.weekday == weekday) return

        val position = plan.schedule
            .filter { it.weekday == weekday }
            .maxOfOrNull { it.position }
            ?.plus(1) ?: 0

        plans[planId] = plan.copy(
            schedule = plan.schedule.map {

                if (it.id == scheduleId) {
                    it.copy(
                        weekday = weekday,
                        position = position
                    )
                } else {
                    it
                }
            }
        )
    }

    // Gestión de entrenamientos

    override suspend fun getExerciseContext(
        exerciseId: String
    ): ExerciseContext? {

        return exercises.find {
            it.id == exerciseId
        }
    }

    override suspend fun getWorkout(
        workoutId: String
    ): Workout? {

        return workouts[workoutId]
    }

    override suspend fun createWorkout(
        name: String
    ): Workout {

        val workout = Workout(
            generateId(),
            name,
            image = Res.drawable.training_muscles
        )

        workouts[workout.id] = workout

        return workout
    }

    override suspend fun renameWorkout(
        workoutId: String,
        name: String
    ) {

        val workout = requireActiveWorkout(workoutId)

        workouts[workoutId] = workout.copy(
            name = name
        )
    }

    override suspend fun removeWorkout(workoutId: String) {

        requireActiveWorkout(workoutId)

        archivedWorkoutIds.add(workoutId)
    }

    // Gestión de ejercicios de entrenamiento

    override suspend fun addWorkoutExercise(
        workoutId: String,
        exerciseId: String,
        numReps: Int,
        numSeries: Int,
        weight: Double,
        restSec: Int
    ): WorkoutExercise {

        val workout = requireActiveWorkout(workoutId)

        val exercise = requireNotNull(
            exercises.find { it.id == exerciseId }
        ) {
            "El ejercicio no existe o no esta activo"
        }

        val workoutExercise = WorkoutExercise(
            generateId(),
            exerciseId,
            exercise.name,
            weight,
            numReps,
            numSeries,
            restSec
        )

        workouts[workoutId] = workout.copy(
            workoutExercises =
                workout.workoutExercises + workoutExercise
        )

        return workoutExercise
    }

    override suspend fun updateWorkoutExercise(
        workoutId: String,
        workoutExercise: WorkoutExercise
    ) {

        val workout = requireActiveWorkout(workoutId)
        val existing = requireNotNull(workout.workoutExercises.find { it.id == workoutExercise.id }) {
            "El ejercicio no pertenece a la rutina"
        }

        require(
            existing.exerciseId == workoutExercise.exerciseId
        ) {
            "No se puede cambiar el ejercicio del catalogo"
        }

        workouts[workoutId] = workout.copy(
            workoutExercises = workout.workoutExercises.map {

                if (it.id == existing.id) {

                    existing.copy(
                        weight = workoutExercise.weight,
                        numReps = workoutExercise.numReps,
                        numSets = workoutExercise.numSets,
                        restSec = workoutExercise.restSec
                    )

                } else {
                    it
                }
            }
        )
    }

    override suspend fun removeWorkoutExercise(
        workoutId: String,
        workoutExerciseId: String
    ) {

        val workout = requireActiveWorkout(workoutId)
        require(workout.workoutExercises.any { it.id == workoutExerciseId }) { "El ejercicio no pertenece a la rutina" }
        workouts[workoutId] = workout.copy(workoutExercises = workout.workoutExercises.filterNot { it.id == workoutExerciseId })
    }

    override suspend fun getAllWorkouts(): List<Workout> {

        return workouts.values.filterNot {
            it.id in archivedWorkoutIds
        }
    }

    private fun requireActiveWorkout(workoutId: String): Workout {
        val workout = requireNotNull(workouts[workoutId]) { "La rutina no existe o no pertenece al usuario" }
        require(workoutId !in archivedWorkoutIds) { "La rutina esta archivada" }
        return workout
    }
}
