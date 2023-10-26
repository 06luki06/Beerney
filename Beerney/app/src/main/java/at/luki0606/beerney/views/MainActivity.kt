package at.luki0606.beerney.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.views.beerList.BeerList
import at.luki0606.beerney.views.beerMap.BeerMap
import at.luki0606.beerney.views.findHome.FindHome
import at.luki0606.beerney.views.navigation.NavigationBar
import at.luki0606.beerney.views.statistics.Statistics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeerneyTheme {
                BuildView()
            }
        }
    }
}

@Composable
fun BuildView(){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Alabaster),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var selectedIndex by remember { mutableIntStateOf(0) }

        when (selectedIndex) {
            0 -> BeerMap()
            1 -> BeerList()
            2 -> Statistics()
            3 -> FindHome()
            else -> BeerMap()
        }

        NavigationBar(selectedIndex){newIndex -> selectedIndex = newIndex}
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    BuildView()
}

