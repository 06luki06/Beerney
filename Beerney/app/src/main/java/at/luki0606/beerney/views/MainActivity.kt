package at.luki0606.beerney.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import at.luki0606.beerney.views.beerList.BeerList
import at.luki0606.beerney.views.beerMap.BeerMap
import at.luki0606.beerney.views.findHome.FindHome
import at.luki0606.beerney.views.navigation.NavigationBar
import at.luki0606.beerney.views.statistics.Statistics

class MainActivity : ComponentActivity() {
    private val beerListViewModel: BeerListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeerneyTheme {
                BuildView(beerListViewModel)
            }
        }
    }
}

@Composable
fun BuildView(beerListViewModel: BeerListViewModel){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Alabaster),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var selectedIndex by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .weight(1f)
        ){
            when (selectedIndex) {
                0 -> BeerMap()
                1 -> BeerList(beerListViewModel)
                2 -> Statistics()
                3 -> FindHome()
                else -> BeerMap()
            }
        }

        NavigationBar(selectedIndex){newIndex -> selectedIndex = newIndex}
    }
}
