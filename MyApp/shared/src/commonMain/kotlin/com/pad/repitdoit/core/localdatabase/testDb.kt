package com.pad.repitdoit.core.localdatabase

class testDb(
    private val database: RepItDoItDatabase
) {
    fun seed() {
        val userId = "test-user-001"
        val planId = "test-plan-001"

        val existingPlan = database.trainingPlanQueries
            .selectPlanById(planId, userId)
            .executeAsOneOrNull()

        if (existingPlan != null) return

        database.transaction {

            // 1. Usuario
            val user = database.userQueries
                .selectUserById(userId)
                .executeAsOneOrNull()

            if (user == null) {
                database.userQueries.insertUser(
                    id = userId,
                    username = "TestUser",
                    display_name = "Usuario de prueba",
                    last_synced_at = null
                )
            }

            // 2. Ejercicios del catálogo
            database.exerciseQueries.insertExercise(
                id = "test-ex-001",
                name = "Press banca",
                description = "Press de banca con barra",
                image_url = null,
                video_url = null,
                is_active = 1L,
                updated_at = 1000L
            )

            database.exerciseQueries.insertExercise(
                id = "test-ex-002",
                name = "Press militar",
                description = "Press de hombros",
                image_url = null,
                video_url = null,
                is_active = 1L,
                updated_at = 1000L
            )

            // 3. Rutinas
            database.workoutQueries.insertWorkout(
                id = "test-workout-001",
                user_id = userId,
                name = "Push Day",
                description = "Entrenamiento de empuje",
                image_url = "training_muscles",
                estimated_time = 60L,
                created_at = 1000L,
                updated_at = 1000L
            )

            database.workoutQueries.insertWorkout(
                id = "test-workout-002",
                user_id = userId,
                name = "Cardio",
                description = "Entrenamiento cardiovascular",
                image_url = null,
                estimated_time = 30L,
                created_at = 1000L,
                updated_at = 1000L
            )

            // 4. Ejercicios asociados a Push Day
            database.workoutExerciseQueries
                .insertWorkoutExercise(
                    id = "test-we-001",
                    workout_id = "test-workout-001",
                    exercise_id = "test-ex-001",
                    position = 0L,
                    target_weight = 20.0,
                    target_sets = 4L,
                    target_reps = 10L,
                    rest_seconds = 90L,
                    notes = null
                )

            database.workoutExerciseQueries
                .insertWorkoutExercise(
                    id = "test-we-002",
                    workout_id = "test-workout-001",
                    exercise_id = "test-ex-002",
                    position = 1L,
                    target_weight = 20.0,
                    target_sets = 3L,
                    target_reps = 12L,
                    rest_seconds = 60L,
                    notes = null
                )

            // 5. Plan de entrenamiento
            database.trainingPlanQueries.insertTrainingPlan(
                id = planId,
                user_id = userId,
                name = "Plan de prueba",
                created_at = 1000L,
                updated_at = 1000L
            )

            // Desactivar cualquier plan anterior
            database.trainingPlanQueries.deactivateUserPlans(
                updated_at = 1000L,
                user_id = userId
            )

            // Activar el plan de prueba
            database.trainingPlanQueries.activateTrainingPlan(
                updated_at = 1000L,
                id = planId,
                user_id = userId
            )

            // 6. Programar ambas rutinas
            // para todos los días de la semana
            for (day in 1L..7L) {

                database.workoutScheduleQueries
                    .insertWorkoutSchedule(
                        id = "test-push-$day",
                        user_id = userId,
                        training_plan_id = planId,
                        workout_id = "test-workout-001",
                        weekday = day,
                        position = 0L
                    )

                database.workoutScheduleQueries
                    .insertWorkoutSchedule(
                        id = "test-cardio-$day",
                        user_id = userId,
                        training_plan_id = planId,
                        workout_id = "test-workout-002",
                        weekday = day,
                        position = 1L
                    )
            }
        }
    }
}
