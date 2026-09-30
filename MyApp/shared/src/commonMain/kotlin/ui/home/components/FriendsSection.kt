package ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FriendsSection(
    friendName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String = "Acepta el desafío de hoy"
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Amigos",
            style = MaterialTheme.typography.titleMediumEmphasized
        )
        ActionCard(
            title = "$friendName te ha retado",
            subtitle = subtitle,
            onClick = onClick,
            highlightedArrow = false,
            leadingContent = {
                WorkoutPlayIcon(
                    isChallenge = true,
                    containerColor = Color(0xFFE7F4E9),
                    contentColor = Color(0xFF4C855B)
                )
            }
        )
    }
}
