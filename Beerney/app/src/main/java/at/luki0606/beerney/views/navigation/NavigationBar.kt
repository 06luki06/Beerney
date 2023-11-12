package at.luki0606.beerney.views.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.ui.theme.Black

@Preview(showBackground = true)
@Composable
fun Preview() {
    BeerneyTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }

        NavigationBar(selectedIndex) { newIndex ->
            selectedIndex = newIndex
        }
    }
}

@Composable
fun NavigationBar(selectedIndex: Int, onIndexChanged: (Int) -> Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        NavigationBarItem(
            icon = Icons.Rounded.List,
            contentDesc = "List",
            selected = selectedIndex == 0,
        ){
            onIndexChanged(0)
        }
        NavigationBarItem(
            icon = Icons.Rounded.LocationOn,
            contentDesc = "Home",
            selected = selectedIndex == 1,
            ){
            onIndexChanged(1)
        }
        NavigationBarItem(
            icon = Icons.Rounded.Star,
            contentDesc = "Statistics",
            selected = selectedIndex == 2,
            ){
            onIndexChanged(2)
        }
        NavigationBarItem(
            icon = Icons.Rounded.Home,
            contentDesc = "FindHome",
            selected = selectedIndex == 3,
            ){
            onIndexChanged(3)
        }
    }
}

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
