package com.pad.repitdoit.features.training.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.pad.repitdoit.core.utility.loadImage
import com.pad.repitdoit.features.training.domain.model.WorkoutExercise
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

// Resume un entrenamiento y ofrece acciones para consultarlo, iniciarlo o eliminarlo.
@Composable
fun TrainingCard(
    title: String,
    exercises: List<WorkoutExercise> = emptyList(),
    image: DrawableResource,
    xp: Long,
    onDetails: () -> Unit,
    onStart: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    OutlinedCard(
        onClick = onDetails,
        border =
            if (isSelected) BorderStroke(
                width =2.dp,
                color = MaterialTheme.colorScheme.primary
            ) else CardDefaults.outlinedCardBorder(),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = onRemove,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        painterResource(loadImage("iconTrash")),
                        contentDescription = "Delete Button"
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1.5f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                exercises.forEach { exercise ->
                    ExerciseView(exercise)
                }
            }
            // Usa la imagen de la categoría recibida para ilustrar el entrenamiento.
            Image(
                painter = painterResource(image),
                contentDescription = "Grupos musculares del entrenamiento",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "+$xp XP",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onStart,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(loadImage("icon_play")),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Iniciar")
                }
            }
        }
    }
}

@Composable
private fun ExerciseView(exercise: WorkoutExercise) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "${exercise.numSets} x ${exercise.numReps} repeticiones · ${exercise.weight} kg",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
