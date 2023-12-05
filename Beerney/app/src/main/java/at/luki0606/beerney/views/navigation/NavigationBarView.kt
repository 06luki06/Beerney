package at.luki0606.beerney.views.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationBarView(selectedIndex: Int, onIndexChanged: (Int) -> Unit){
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