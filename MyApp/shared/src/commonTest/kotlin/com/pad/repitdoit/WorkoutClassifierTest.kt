package com.pad.repitdoit

import com.pad.repitdoit.features.training.domain.logic.WorkoutClassifier
import com.pad.repitdoit.features.training.domain.model.MuscleGroup
import com.pad.repitdoit.features.training.domain.model.MuscleGroup.*
import com.pad.repitdoit.features.training.domain.model.WorkoutCategory
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import com.pad.repitdoit.features.training.domain.model.Workout
import kotlin.test.Test
import kotlin.test.assertEquals

class WorkoutClassifierTest {
    private fun exercises(vararg groups: Set<MuscleGroup>): List<WorkoutExercise> =
        groups.mapIndexed { index, muscles ->
            WorkoutExercise("$index", "exercise-$index", "Exercise $index",
                0.0, 10, 3, 60, muscles)
        }

    private fun checkCategory(expected: WorkoutCategory, vararg groups: Set<MuscleGroup>) {
        assertEquals(expected, WorkoutClassifier.classify(exercises(*groups)))
    }

    @Test
    fun specificMuscles() {
        checkCategory(WorkoutCategory.CHEST, setOf(CHEST), setOf(CHEST, TRICEPS))
        checkCategory(WorkoutCategory.BACK, setOf(BACK), setOf(BACK, BICEPS))
        checkCategory(WorkoutCategory.SHOULDERS, setOf(SHOULDERS), setOf(REAR_SHOULDERS))
        checkCategory(WorkoutCategory.BICEPS, setOf(BICEPS, FOREARMS))
        checkCategory(WorkoutCategory.TRICEPS, setOf(TRICEPS))
        checkCategory(WorkoutCategory.CORE, setOf(CORE))
        checkCategory(WorkoutCategory.LOWER_BODY, setOf(LEGS), setOf(LEGS, CORE))
        checkCategory(WorkoutCategory.LOWER_BODY, setOf(LEGS, CORE))
        checkCategory(WorkoutCategory.LOWER_BODY, setOf(LEGS, CORE), setOf(LEGS, CORE, BACK))
    }

    @Test
    fun combinations() {
        checkCategory(WorkoutCategory.PUSH, setOf(CHEST, SHOULDERS, TRICEPS), setOf(SHOULDERS, TRICEPS))
        checkCategory(WorkoutCategory.PULL, setOf(BACK, REAR_SHOULDERS, BICEPS), setOf(BICEPS, FOREARMS))
        checkCategory(WorkoutCategory.ARMS, setOf(BICEPS, FOREARMS), setOf(TRICEPS))
        checkCategory(WorkoutCategory.UPPER_BODY, setOf(CHEST, SHOULDERS, TRICEPS), setOf(BACK, BICEPS))
        checkCategory(WorkoutCategory.FULL_BODY, setOf(CHEST, SHOULDERS, TRICEPS), setOf(LEGS, CORE))
    }

    @Test
    fun emptyUnknownAndAmbiguous() {
        checkCategory(WorkoutCategory.DEFAULT)
        checkCategory(WorkoutCategory.DEFAULT, emptySet())
        checkCategory(WorkoutCategory.DEFAULT, setOf(CHEST), emptySet(), emptySet())
        checkCategory(WorkoutCategory.DEFAULT, setOf(CHEST), setOf(CORE))
        checkCategory(WorkoutCategory.DEFAULT, setOf(LEGS, CORE, BACK))
        checkCategory(WorkoutCategory.CHEST, setOf(CHEST), setOf(CHEST), emptySet())
    }

    @Test
    fun categoryUpdatesAndDoesNotDependOnNameOrOrder() {
        val items = exercises(setOf(CHEST, SHOULDERS, TRICEPS), setOf(BACK, BICEPS))
        val workout = Workout("1", "Push day", items)
        assertEquals(WorkoutCategory.UPPER_BODY, workout.category)
        assertEquals(workout.category, workout.copy(name = "Leg day").category)
        assertEquals(workout.category, workout.copy(workoutExercises = items.reversed()).category)
        assertEquals(WorkoutCategory.PUSH, workout.copy(workoutExercises = items.take(1)).category)
        assertEquals(WorkoutCategory.DEFAULT, workout.copy(workoutExercises = emptyList()).category)
    }
}
