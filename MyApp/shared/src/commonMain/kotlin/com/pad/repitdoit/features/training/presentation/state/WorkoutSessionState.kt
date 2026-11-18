package com.pad.repitdoit.features.training.presentation.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.di.TrainingUseCasesAC
import com.pad.repitdoit.features.training.domain.model.Workout
import kotlinx.coroutines.CancellationException
import kotlin.time.Clock

@Stable
internal class WorkoutSessionState {
    private var sessionId = generateId()
    var result by mutableStateOf<Result<Workout?>?>(null)
    var completionResult by mutableStateOf<Result<Unit>?>(null)
        private set
    var completionAttempt by mutableIntStateOf(0)
    var loadAttempt by mutableIntStateOf(0)
    var exerciseIndex by mutableIntStateOf(0)
        private set
    var configuredSeconds by mutableIntStateOf(0)
    var pausedMillis by mutableLongStateOf(0L)
    var deadline by mutableStateOf<Long?>(null)
    var restStarted by mutableStateOf(false)
    var startedAt by mutableLongStateOf(0L)
        private set
    var finishedElapsed by mutableStateOf<Int?>(null)

    fun initialize(workout: Workout) {
        if (startedAt == 0L || exerciseIndex !in workout.workoutExercises.indices) {
            selectExercise(workout, exerciseIndex.coerceIn(workout.workoutExercises.indices))
            if (startedAt == 0L) startedAt = workoutClockMillis()
        }
    }

    fun selectExercise(workout: Workout, index: Int) {
        exerciseIndex = index
        configuredSeconds = workout.workoutExercises[index].restSec.coerceAtLeast(0)
        pausedMillis = configuredSeconds * 1000L
        deadline = null
        restStarted = false
    }

    suspend fun saveCompletion(appContainer: TrainingUseCasesAC, workout: Workout, duration: Int) {
        completionResult = null
        completionResult = try {
            appContainer.completeWorkoutSessionUseCase(
                sessionId, workout, startedAt, startedAt + duration * 1000L
            )
            Result.success(Unit)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    companion object {
        val Saver = listSaver<WorkoutSessionState, Any>(
            save = {
                listOf(
                    it.exerciseIndex, it.configuredSeconds, it.pausedMillis,
                    it.deadline ?: -1L, it.restStarted, it.startedAt, it.finishedElapsed ?: -1,
                    it.sessionId
                )
            },
            restore = { values ->
                WorkoutSessionState().apply {
                    exerciseIndex = values[0] as Int
                    configuredSeconds = values[1] as Int
                    pausedMillis = values[2] as Long
                    deadline = (values[3] as Long).takeIf { it >= 0 }
                    restStarted = values[4] as Boolean
                    startedAt = values[5] as Long
                    finishedElapsed = (values[6] as Int).takeIf { it >= 0 }
                    sessionId = values.getOrNull(7) as? String ?: sessionId
                }
            }
        )
    }
}

@Composable
internal fun rememberWorkoutSessionState(workoutId: String, appContainer: TrainingUseCasesAC): WorkoutSessionState {
    val session = rememberSaveable(workoutId, saver = WorkoutSessionState.Saver) { WorkoutSessionState() }
    LaunchedEffect(session, session.loadAttempt) {
        session.result = null
        session.result = try {
            val workout = appContainer.getWorkoutUseCase(workoutId)
            if (workout != null && workout.workoutExercises.isNotEmpty()) session.initialize(workout)
            Result.success(workout)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
    val workout = session.result?.getOrNull()
    val duration = session.finishedElapsed
    LaunchedEffect(session, workout, duration, session.completionAttempt) {
        if (workout != null && duration != null) {
            session.saveCompletion(appContainer, workout, duration)
        }
    }
    return session
}

internal fun workoutClockMillis(): Long = Clock.System.now().toEpochMilliseconds()
