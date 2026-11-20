package com.pad.repitdoit.features.training.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.ui.common.OnBackHandler
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.presentation.state.WorkoutSessionState
import com.pad.repitdoit.features.training.presentation.state.workoutClockMillis
import com.pad.repitdoit.features.training.presentation.layout.WorkoutSessionLayout
import kotlinx.coroutines.delay

// Coordina la sesión de entrenamiento desde la carga hasta el resumen final.
@Composable
internal fun WorkoutSessionScreenRoot(
    session: WorkoutSessionState,
    isCurrent: Boolean,
    onExit: () -> Unit
) {
    var confirmExit by remember(session) { mutableStateOf(false) }
    val duration = session.finishedElapsed
    val completion = session.completionResult
    OnBackHandler(enabled = isCurrent) {
        if (duration == null) confirmExit = !confirmExit
        else if (completion?.isSuccess == true) onExit()
    }

    // Pide confirmación antes de abandonar la sesión en curso.
    if (confirmExit && isCurrent) {
        AlertDialog(
            onDismissRequest = { confirmExit = false },
            title = { Text("¿Salir del entrenamiento?") },
            text = { Text("Volverás a la pestaña de entrenamiento y se cerrará el temporizador.") },
            confirmButton = { TextButton(onClick = onExit) { Text("Salir") } },
            dismissButton = {
                TextButton(onClick = { confirmExit = false }) { Text("Continuar") }
            }
        )
    }

    val result = session.result
    val workout = result?.getOrNull()
    if (workout != null && workout.workoutExercises.isNotEmpty()) {
        if (duration == null) {
            session.WorkoutSession(workout, isCurrent, onExitRequested = { confirmExit = true })
        } else if (completion?.isSuccess == true) {
            WorkoutSessionSummaryScreen(workout, duration, onExit)
        } else {
            Box(Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center) {
                if (completion == null) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("No se pudo guardar el entrenamiento.")
                        Button(onClick = { session.completionAttempt++ }) { Text("Reintentar") }
                        TextButton(onClick = { confirmExit = true }) { Text("Salir") }
                    }
                }
            }
        }
        return
    }

    Box(Modifier.fillMaxSize().safeDrawingPadding().padding(20.dp), contentAlignment = Alignment.Center) {
        if (result == null) {
            CircularProgressIndicator()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    when {
                        result?.isFailure == true -> "No se pudo cargar el entrenamiento."
                        workout == null -> "El entrenamiento no está disponible."
                        else -> "Añade ejercicios al entrenamiento para empezar."
                    }
                )
                if (result?.isFailure == true) {
                    Button(onClick = { session.loadAttempt++ }) { Text("Reintentar") }
                }
                TextButton(onClick = onExit) { Text("Volver") }
            }
        }
    }
}

@Composable
private fun WorkoutSessionState.WorkoutSession(
    workout: Workout,
    isCurrent: Boolean,
    onExitRequested: () -> Unit
) {
    val exercises = workout.workoutExercises
    val currentIndex = exerciseIndex
    var now by remember { mutableLongStateOf(workoutClockMillis()) }
    val elapsedSeconds = ((now - startedAt).coerceAtLeast(0) / 1000).toInt()

    fun updateRestTime(seconds: Long) {
        if (seconds !in 0..Int.MAX_VALUE.toLong()) return
        configuredSeconds = seconds.toInt()
        pausedMillis = seconds * 1000L
        deadline = null
        restStarted = false
    }

    val running = deadline != null
    val remainingMillis = deadline?.let { (it - now).coerceAtLeast(0) } ?: pausedMillis
    val remaining = ((remainingMillis + 999L) / 1000).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

    // Deriving the countdown from the deadline avoids accumulating delay between ticks.
    // Actualiza la cuenta atrás mientras la sesión está visible.
    LaunchedEffect(isCurrent, deadline) {
        if (!isCurrent) return@LaunchedEffect
        while (true) {
            now = workoutClockMillis()
            val end = deadline
            if (end != null && now >= end) {
                pausedMillis = 0
                deadline = null
            }
            delay(if (deadline != null) 100L else 1000L)
        }
    }

    WorkoutSessionLayout(
        workout = workout,
        exerciseIndex = currentIndex,
        elapsedSeconds = elapsedSeconds,
        restSeconds = remaining,
        restProgress = if (configuredSeconds > 0) (remainingMillis / (configuredSeconds * 1000f)).coerceIn(0f, 1f) else 0f,
        running = running,
        restStarted = restStarted,
        onMinutesChange = { minutes -> updateRestTime(minutes * 60L + remaining % 60) },
        onSecondsChange = { seconds -> updateRestTime((remaining / 60) * 60L + seconds) },
        onReset = {
            deadline = null
            pausedMillis = configuredSeconds * 1000L
            restStarted = false
        },
        onToggleTimer = {
            val instant = workoutClockMillis()
            now = instant
            val end = deadline
            if (end != null) {
                pausedMillis = (end - instant).coerceAtLeast(0)
                deadline = null
            } else if (pausedMillis > 0) {
                restStarted = true
                deadline = instant + pausedMillis
            }
        },
        onExit = onExitRequested,
        onPrevious = { selectExercise(workout, currentIndex - 1) },
        onNext = {
            if (currentIndex < exercises.lastIndex) {
                selectExercise(workout, currentIndex + 1)
            } else {
                finishedElapsed = ((workoutClockMillis() - startedAt).coerceAtLeast(0) / 1000).toInt()
            }
        }
    )
}
