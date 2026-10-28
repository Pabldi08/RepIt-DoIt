package com.pad.repitdoit.features.training.presentation

import org.jetbrains.compose.resources.DrawableResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_legs
import repitdoit.shared.generated.resources.training_muscles

// Datos para el mockup. Sustituir por el estado del ViewModel.
data class TrainingSetPreview(val reps: Int, val weightKg: Int)
data class TrainingHistoryPreview(val date: String, val sets: String, val weight: String)
data class TrainingExercisePreview(
    val id: String,
    val name: String,
    val rest: String,
    val sets: List<TrainingSetPreview>,
    val history: List<TrainingHistoryPreview>
)
data class TrainingWorkoutPreview(
    val id: String,
    val title: String,
    val day: String,
    val minutes: Int,
    val xp: Int,
    val image: DrawableResource,
    val exercises: List<TrainingExercisePreview>
)
data class TrainingDayPreview(val label: String, val workoutId: String?)
data class TrainingPlanPreview(
    val id: String,
    val name: String,
    val days: List<TrainingDayPreview>,
    val workouts: List<TrainingWorkoutPreview>
)

private fun previewExercise(id: String, name: String, weight: Int, reps: Int = 10) =
    TrainingExercisePreview(
        id, name, "90 s",
        List(4) { TrainingSetPreview(reps, weight) },
        listOf(
            TrainingHistoryPreview("19 oct 2026", "4 × $reps", "$weight kg"),
            TrainingHistoryPreview("12 oct 2026", "4 × $reps", "${(weight - 2).coerceAtLeast(0)} kg"),
            TrainingHistoryPreview("5 oct 2026", "4 × $reps", "${(weight - 4).coerceAtLeast(0)} kg")
        )
    )

object TrainingPreviewData {
    val plan = TrainingPlanPreview(
        id = "demo-strength-week",
        name = "Fuerza",
        days = listOf(
            TrainingDayPreview("Lunes", "demo-upper"),
            TrainingDayPreview("Martes", null),
            TrainingDayPreview("Miércoles", "demo-legs"),
            TrainingDayPreview("Jueves", null),
            TrainingDayPreview("Viernes", "demo-full"),
            TrainingDayPreview("Sábado", null),
            TrainingDayPreview("Domingo", null)
        ),
        workouts = listOf(
            TrainingWorkoutPreview("demo-upper", "Tren superior", "Lunes", 50, 200, Res.drawable.training_muscles, listOf(
                previewExercise("bench", "Press de banca", 40),
                previewExercise("row", "Remo con mancuerna", 16, 12),
                previewExercise("pulldown", "Jalón al pecho", 50),
                previewExercise("curl", "Curl de bíceps", 20)
            )),
            TrainingWorkoutPreview("demo-legs", "Pierna", "Miércoles", 45, 150, Res.drawable.training_legs, listOf(
                previewExercise("leg-press", "Press de piernas", 150),
                previewExercise("abductor", "Abductores", 40, 12),
                previewExercise("extension", "Extensión de cuádriceps", 80)
            )),
            TrainingWorkoutPreview("demo-full", "Cuerpo completo", "Viernes", 55, 250, Res.drawable.training_muscles, listOf(
                previewExercise("squat", "Sentadilla", 40),
                previewExercise("bench", "Press de banca", 30, 12),
                previewExercise("row", "Remo con mancuerna", 14, 12)
            ))
        )
    )

    fun workout(id: String) = plan.workouts.find { it.id == id }
}
