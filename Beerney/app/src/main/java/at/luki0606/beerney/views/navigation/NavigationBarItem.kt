package at.luki0606.beerney.views.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.Black

@Composable
fun NavigationBarItem(
    icon: ImageVector,
    contentDesc: String,
    selected: Boolean,
    onClick: () -> Unit)
{
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.secondary
    } else {
        Color.Transparent
    }

    val contentColor = if (selected) {
        Alabaster
    } else {
        Black
    }

    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(horizontal = 25.dp)
    ) {
        IconButton(
            onClick = { onClick() },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = contentColor
            )
        }
    }
}