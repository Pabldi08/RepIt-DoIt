package com.pad.repitdoit.features.training.domain.logic

import com.pad.repitdoit.features.training.domain.model.MuscleGroup
import com.pad.repitdoit.features.training.domain.model.MuscleGroup.*
import com.pad.repitdoit.features.training.domain.model.WorkoutCategory
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise

// Determina la categoría de un entrenamiento a partir de sus ejercicios.
object WorkoutClassifier {
    private const val MIN_KNOWN = 0.60
    private const val DOMINANT = 0.60
    private const val COMBINATION = 0.75
    private const val EPSILON = 1e-9

    fun classify(exercises: List<WorkoutExercise>): WorkoutCategory {
        val known = exercises.filter { it.muscleGroups.isNotEmpty() }
        if (known.isEmpty() || known.size.toDouble() / exercises.size < MIN_KNOWN) {
            return WorkoutCategory.DEFAULT
        }

        // Cada ejercicio aporta un punto, repartido entre sus grupos distintos.
        val scores = mutableMapOf<MuscleGroup, Double>()
        for (exercise in known) {
            val share = 1.0 / exercise.muscleGroups.size
            for (group in exercise.muscleGroups) {
                scores[group] = (scores[group] ?: 0.0) + share
            }
        }

        fun ratio(vararg groups: MuscleGroup): Double =
            groups.sumOf { scores[it] ?: 0.0 } / known.size

        fun atLeast(value: Double, threshold: Double): Boolean =
            value + EPSILON >= threshold

        val chest = ratio(CHEST)
        val back = ratio(BACK)
        val shoulders = ratio(SHOULDERS, REAR_SHOULDERS)
        val biceps = ratio(BICEPS)
        val triceps = ratio(TRICEPS)
        val forearms = ratio(FOREARMS)
        val legs = ratio(LEGS)
        val core = ratio(CORE)
        val arms = biceps + triceps + forearms
        val push = ratio(CHEST, SHOULDERS, TRICEPS)
        val pull = ratio(BACK, REAR_SHOULDERS, BICEPS, FOREARMS)
        val upper = push + pull

        // 1. Priorizar las rutinas con un grupo claramente dominante.
        if (atLeast(chest, DOMINANT)) return WorkoutCategory.CHEST
        if (atLeast(back, DOMINANT)) return WorkoutCategory.BACK
        if (atLeast(shoulders, DOMINANT)) return WorkoutCategory.SHOULDERS
        if (atLeast(biceps, DOMINANT)) return WorkoutCategory.BICEPS
        if (atLeast(triceps, DOMINANT)) return WorkoutCategory.TRICEPS
        if (atLeast(core, DOMINANT)) return WorkoutCategory.CORE
        if (atLeast(legs, DOMINANT)) return WorkoutCategory.LOWER_BODY

        // Sentadillas y otros ejercicios de piernas pueden incluir la zona lumbar.
        if (atLeast(legs, 0.40) && atLeast(legs + core, COMBINATION) &&
            atLeast(legs, core)
        ) return WorkoutCategory.LOWER_BODY

        // 2. Brazos: permitir antebrazos como soporte de un curl.
        if (atLeast(biceps + forearms, COMBINATION) &&
            atLeast(biceps, 0.25) && triceps < 0.10
        ) return WorkoutCategory.BICEPS

        if (atLeast(triceps + forearms, COMBINATION) &&
            atLeast(triceps, 0.25) && biceps < 0.10
        ) return WorkoutCategory.TRICEPS

        if (atLeast(arms, COMBINATION)) return WorkoutCategory.ARMS

        // 3. Combinaciones de tren superior e inferior.
        val hasUpperExercise = known.any { exercise ->
            LEGS !in exercise.muscleGroups &&
                exercise.muscleGroups.any { it != CORE }
        }
        val hasLowerExercise = known.any { LEGS in it.muscleGroups }
        if (hasUpperExercise && hasLowerExercise &&
            atLeast(upper, 0.25) && atLeast(legs, 0.25) &&
            atLeast(upper + legs, 0.65)
        ) return WorkoutCategory.FULL_BODY

        // 4. Empuje y traccion.
        if (atLeast(push, COMBINATION)) return WorkoutCategory.PUSH
        if (atLeast(pull, COMBINATION)) return WorkoutCategory.PULL
        if (atLeast(upper, COMBINATION) &&
            atLeast(push, 0.20) && atLeast(pull, 0.20)
        ) return WorkoutCategory.UPPER_BODY

        // 5. No hay una combinacion suficientemente representativa.
        return WorkoutCategory.DEFAULT
    }
}
