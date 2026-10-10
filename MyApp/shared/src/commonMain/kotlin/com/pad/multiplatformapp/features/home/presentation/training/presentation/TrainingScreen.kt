package com.pad.multiplatformapp.features.home.presentation.training.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.AppWindowSize
import com.pad.multiplatformapp.features.home.presentation.HomeExtendedLayout
import com.pad.multiplatformapp.features.home.presentation.HomeMediumLayout
import com.pad.multiplatformapp.ui.common.Header
import myapp.shared.generated.resources.Res
import myapp.shared.generated.resources.placeholder
import myapp.shared.generated.resources.training_muscles
import myapp.shared.generated.resources.training_legs
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrainingScreen(
    windowSize: AppWindowSize
) {
    when (windowSize) {
        AppWindowSize.Compact -> TrainingCompactLayout()
        AppWindowSize.Medium -> TrainingCompactLayout()
        AppWindowSize.Extended -> TrainingExtendedLayout()
    }
}

@Composable
fun TrainingCompactLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp)
        )
        Text(
            text = "Mis entrenamientos",
            style = MaterialTheme.typography.titleMediumEmphasized,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrainingCard(
                    title = "Tren Superior",
                    xp = 200,
                    day = "HOY",
                    image = Res.drawable.training_muscles,
                    exercises = listOf(
                        ExerciseItem("Press de banca", "3x10 repeticiones · 40 kg"),
                        ExerciseItem("Remo con mancuerna", "3x12 repeticiones · 16 kg"),
                        ExerciseItem(name = "Jalón al pecho", "4x10 repeticiones · 50 kg"),
                        ExerciseItem("Curl de bíceps", "4x10 repeticiones · 20 kg")
                    )
                )
            }
            item {
                TrainingCard(
                    title = "Pierna",
                    xp = 150,
                    day = "MAÑANA",
                    image = Res.drawable.training_legs,
                    exercises = listOf(
                        ExerciseItem("Press de piernas", "3x10 repeticiones · 150 kg"),
                        ExerciseItem("Abductores", "3x12 repeticiones · 40 kg"),
                        ExerciseItem("Extensión de cuádriceps", "4x10 repeticiones · 80 kg")
                    )
                )
            }
            item {
                OutlinedCard(
                    onClick = { /* Aquí irá la acción de añadir */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Añadir entrenamiento",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrainingExtendedLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp)
        )

        Text(
            text = "Mis entrenamientos",
            style = MaterialTheme.typography.titleMediumEmphasized,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrainingCard(
                    title = "Tren Superior",
                    xp = 200,
                    day = "HOY",
                    image = Res.drawable.training_muscles,
                    exercises = listOf(
                        ExerciseItem("Press de banca", "3x10 repeticiones · 40 kg"),
                        ExerciseItem("Remo con mancuerna", "3x12 repeticiones · 16 kg"),
                        ExerciseItem(name = "Jalón al pecho", "4x10 repeticiones · 50 kg"),
                        ExerciseItem("Curl de bíceps", "4x10 repeticiones · 20 kg")
                    )
                )
            }
            item {
                TrainingCard(
                    title = "Pierna",
                    xp = 150,
                    day = "MAÑANA",
                    image = Res.drawable.training_legs,
                    exercises = listOf(
                        ExerciseItem("Press de piernas", "3x10 repeticiones · 150 kg"),
                        ExerciseItem("Abductores", "3x12 repeticiones · 40 kg"),
                        ExerciseItem("Extensión de cuádriceps", "4x10 repeticiones · 80 kg")
                    )
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedCard(
                    onClick = { /* Aquí irá la acción de añadir */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Añadir entrenamiento",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

