package com.pad.repitdoit.features.training.presentation.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.pad.repitdoit.features.training.domain.model.Workout
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import org.jetbrains.compose.resources.painterResource
import repitdoit.shared.generated.resources.Res
import repitdoit.shared.generated.resources.training_timer_track_compact
import repitdoit.shared.generated.resources.training_timer_track_flex
import repitdoit.shared.generated.resources.training_timer_track_wide

private enum class SessionLayout {
    Portrait, Wide, Flex
}

@Composable
internal fun WorkoutSessionLayout(
    workout: Workout,
    exerciseIndex: Int,
    elapsedSeconds: Int,
    restSeconds: Int,
    restProgress: Float,
    running: Boolean,
    restStarted: Boolean,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
    onReset: () -> Unit,
    onToggleTimer: () -> Unit,
    onExit: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val posture = adaptiveInfo.windowPosture
    val windowSize = LocalWindowInfo.current.containerSize
    val isLandscape = windowSize.width > windowSize.height
    val horizontalHinge = posture.hingeList.firstOrNull { !it.isFlat && !it.isVertical }
    val hasPrevious = exerciseIndex > 0
    val hasNext = exerciseIndex < workout.workoutExercises.lastIndex
    val density = LocalDensity.current
    var windowTop by remember { mutableFloatStateOf(Float.NaN) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .onGloballyPositioned { windowTop = it.positionInWindow().y }
    ) {
        val layout = when {
            posture.isTabletop && isLandscape -> SessionLayout.Flex
            adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) ||
                isLandscape -> SessionLayout.Wide
            else -> SessionLayout.Portrait
        }

        val header: @Composable () -> Unit = { SessionHeader(workout.name, elapsedSeconds) }
        val timer: @Composable () -> Unit = {
            key(workout.workoutExercises[exerciseIndex].id) {
                RestTimer(
                    layout, restSeconds, restProgress, running, restStarted,
                    onMinutesChange, onSecondsChange, onReset, onToggleTimer
                )
            }
        }
        val actions: @Composable () -> Unit = {
            SessionActions(hasPrevious, hasNext, layout == SessionLayout.Flex, onExit, onPrevious, onNext)
        }
        val rowHeight = if (layout == SessionLayout.Portrait) 76.dp else 64.dp
        val listState = rememberLazyListState()
        LaunchedEffect(exerciseIndex) {
            if (exerciseIndex != 0 || listState.firstVisibleItemIndex != 0) {
                listState.scrollToItem(exerciseIndex)
            }
        }

        when (layout) {
            SessionLayout.Portrait -> Column(
                Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                header()
                Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    timer()
                }
                ExerciseListHeader(exerciseIndex, workout.workoutExercises.size)
                ExerciseList(workout.workoutExercises, exerciseIndex, Modifier.weight(1f), listState, rowHeight)
                actions()
            }

            SessionLayout.Wide -> Column(
                Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                header()
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(
                        Modifier.weight(0.4f).fillMaxHeight().verticalScroll(rememberScrollState())
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.TopCenter
                    ) { timer() }
                    Column(
                        Modifier.weight(0.6f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExerciseListHeader(exerciseIndex, workout.workoutExercises.size)
                        ExerciseList(workout.workoutExercises, exerciseIndex, Modifier.weight(1f), listState, rowHeight)
                        actions()
                    }
                }
            }

            SessionLayout.Flex -> {
                // Hinge coordinates are window pixels. Convert to dp within the session content.
                val upperHeight = horizontalHinge?.takeIf { windowTop.isFinite() }?.let {
                    with(density) { (it.bounds.top - windowTop).toDp() }.coerceIn(0.dp, maxHeight)
                } ?: (maxHeight / 2)
                val foldGap = horizontalHinge?.let {
                    with(density) { it.bounds.height.toDp() }.coerceIn(0.dp, maxHeight - upperHeight)
                } ?: 0.dp

                Column(Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxWidth().height(upperHeight).padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        header()
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Box(
                                Modifier.weight(0.4f).fillMaxHeight().verticalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) { timer() }
                            Column(Modifier.weight(0.6f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ExerciseListHeader(exerciseIndex, workout.workoutExercises.size)
                                ExerciseList(workout.workoutExercises, exerciseIndex, Modifier.weight(1f), listState, rowHeight)
                            }
                        }
                    }
                    Spacer(Modifier.height(foldGap))
                    Box(
                        Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.surface)
                            .verticalScroll(rememberScrollState()).padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Column(Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("CONTROLES DEL DESCANSO", style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                TimerControls(running, restStarted, restSeconds > 0, true, onReset, onToggleTimer)
                            }
                            Column(Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("ENTRENAMIENTO", style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                actions()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionHeader(name: String, elapsedSeconds: Int) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text(
            "◷  ${twoDigits(elapsedSeconds / 60)}:${twoDigits(elapsedSeconds % 60)}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun RestTimer(
    layout: SessionLayout,
    seconds: Int,
    progress: Float,
    running: Boolean,
    restStarted: Boolean,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
    onReset: () -> Unit,
    onToggle: () -> Unit
) {
    val track = when (layout) {
        SessionLayout.Portrait -> Res.drawable.training_timer_track_compact
        SessionLayout.Wide -> Res.drawable.training_timer_track_wide
        SessionLayout.Flex -> Res.drawable.training_timer_track_flex
    }
    val primary = MaterialTheme.colorScheme.primary
    val minutes = twoDigits(seconds / 60)
    val numberSize = if (minutes.length > 4) 22.sp else if (layout == SessionLayout.Portrait) 36.sp else 44.sp

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val trackPainter = painterResource(track)
        val diameter = with(LocalDensity.current) { trackPainter.intrinsicSize.width.toDp() }
        Box(Modifier.size(diameter), contentAlignment = Alignment.Center) {
            Image(trackPainter, contentDescription = null, modifier = Modifier.matchParentSize())
            if (restStarted && progress > 0f) {
                Canvas(Modifier.matchParentSize()) {
                    val stroke = 5.dp.toPx()
                    drawArc(
                        color = primary, startAngle = -90f, sweepAngle = progress * 360f,
                        useCenter = false, topLeft = Offset(stroke / 2, stroke / 2),
                        size = Size(size.width - stroke, size.height - stroke),
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Top) {
                    TimeField(minutes, "min", "Minutos de descanso", numberSize, (Int.MAX_VALUE - seconds % 60) / 60, onMinutesChange)
                    Text(":", fontSize = numberSize, lineHeight = numberSize * 1.1f, fontWeight = FontWeight.SemiBold)
                    TimeField(twoDigits(seconds % 60), "seg", "Segundos de descanso", numberSize, 59, onSecondsChange)
                }
                if (layout != SessionLayout.Flex) {
                    TimerControls(running, restStarted, seconds > 0, false, onReset, onToggle)
                }
            }
        }
        if (layout == SessionLayout.Wide) {
            Text("Recuperación", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TimeField(
    value: String,
    unit: String,
    label: String,
    numberSize: TextUnit,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf(TextFieldValue(value)) }
    val focusManager = LocalFocusManager.current
    val colors = MaterialTheme.colorScheme
    val digitCount = maxOf(if (focused) input.text.length else value.length, 2)
    val fieldWidth = (numberSize.value * 0.61f * digitCount + 4f).dp.coerceAtLeast(48.dp)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = if (focused) input else TextFieldValue(value),
            onValueChange = { next ->
                val number = next.text.toIntOrNull()
                if (next.text.isEmpty() ||
                    (next.text.length <= maxValue.toString().length && next.text.all { it in '0'..'9' } &&
                        number != null && number in 0..maxValue)
                ) {
                    val changed = input.text != next.text
                    input = next
                    if (changed && number != null) onValueChange(number)
                }
            },
            modifier = Modifier.width(fieldWidth)
                .onFocusChanged { state ->
                    if (state.isFocused && !focused) {
                        input = TextFieldValue(value, TextRange(0, value.length))
                    } else if (!state.isFocused && focused) {
                        input = TextFieldValue(value)
                    }
                    focused = state.isFocused
                }
                .semantics { contentDescription = label },
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = colors.onSurface,
                fontSize = numberSize,
                lineHeight = numberSize * 1.1f,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true
        )
        Text(unit, style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
    }
}

@Composable
private fun TimerControls(
    running: Boolean,
    restStarted: Boolean,
    canStart: Boolean,
    flex: Boolean,
    onReset: () -> Unit,
    onToggle: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SessionButton("↺", "Reiniciar", flex, onReset, modifier = if (flex) Modifier.weight(1f) else Modifier)
        SessionButton(
            if (running) "Ⅱ" else "▶",
            when {
                running -> "Pausar"
                restStarted && canStart -> "Reanudar"
                else -> "Iniciar"
            },
            flex, onToggle,
            modifier = if (flex) Modifier.weight(1f) else Modifier,
            enabled = canStart || running, primary = true, pause = running
        )
    }
}

@Composable
private fun SessionActions(
    hasPrevious: Boolean,
    hasNext: Boolean,
    flex: Boolean,
    onExit: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SessionButton("×", "Salir", flex, onExit, modifier = if (flex) Modifier.weight(1f) else Modifier)
        SessionButton(
            "‹", "Anterior", flex, onPrevious,
            modifier = if (flex) Modifier.weight(1f) else Modifier, enabled = hasPrevious
        )
        SessionButton(
            if (hasNext) "→" else "✓", if (hasNext) "Siguiente" else "Finalizar", flex, onNext,
            modifier = Modifier.weight(if (flex) 1.1f else 1f), primary = true, alwaysShowLabel = true
        )
    }
}

@Composable
private fun SessionButton(
    symbol: String,
    label: String,
    large: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primary: Boolean = false,
    pause: Boolean = false,
    alwaysShowLabel: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val colors = MaterialTheme.colorScheme
    val container = when {
        pause -> Color(0xFFF6CB55)
        primary -> colors.primary
        else -> colors.surfaceContainerHigh
    }
    Button(
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
        enabled = enabled,
        modifier = modifier.heightIn(min = if (large) 104.dp else 48.dp).widthIn(min = 48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = if (primary) colors.onPrimary else colors.onSurface
        ),
        contentPadding = PaddingValues(horizontal = if (large) 4.dp else 8.dp, vertical = 4.dp)
    ) {
        if (large) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(symbol, fontSize = 25.sp)
                Text(label, style = MaterialTheme.typography.labelMedium)
            }
        } else if (alwaysShowLabel) {
            Text("$label  $symbol")
        } else {
            Text(symbol, Modifier.semantics { contentDescription = label }, fontSize = 18.sp)
        }
    }
}

@Composable
private fun ExerciseListHeader(index: Int, count: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text("Ejercicios", style = MaterialTheme.typography.titleMedium)
        Text("${index + 1} de $count", style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ExerciseList(
    exercises: List<WorkoutExercise>,
    current: Int,
    modifier: Modifier,
    state: LazyListState,
    rowHeight: Dp
) {
    LazyColumn(modifier.fillMaxWidth(), state = state, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(exercises, key = { _, exercise -> exercise.id }) { index, exercise ->
            SessionExerciseCard(exercise, index, current, rowHeight)
        }
    }
}

@Composable
private fun SessionExerciseCard(exercise: WorkoutExercise, index: Int, current: Int, rowHeight: Dp) {
    val colors = MaterialTheme.colorScheme
    val selected = index == current
    val next = index == current + 1
    Surface(
        modifier = Modifier.fillMaxWidth().heightIn(min = rowHeight), shape = RoundedCornerShape(12.dp),
        color = if (selected) colors.surfaceContainerHigh else colors.surface,
        border = if (selected) BorderStroke(1.dp, colors.primary) else null
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(exercise.name, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                val weight = exercise.weight.toString().removeSuffix(".0").replace('.', ',')
                Text("${exercise.numSets} series · ${exercise.numReps} reps · $weight kg",
                    style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            }
            if (selected || next) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (selected) colors.primary else colors.surfaceContainerHigh,
                    contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant
                ) {
                    Text(if (selected) "ACTUAL" else "SIGUIENTE", Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

private fun twoDigits(value: Int): String = value.toString().padStart(2, '0')

