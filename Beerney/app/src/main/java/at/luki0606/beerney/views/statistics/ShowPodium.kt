package at.luki0606.beerney.views.statistics

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import at.luki0606.beerney.models.BeerRepository

@Composable
fun ShowPodium(){
    val beerList = BeerRepository.getBeersSortedByBeerCount()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ){
        PodiumColumn(
            rank = 2,
            brand = beerList[1].first,
            amount = beerList[1].second
        )
        PodiumColumn(
            rank = 1,
            brand = beerList[0].first,
            amount = beerList[0].second
        )
        PodiumColumn(
            rank = 3,
            brand = beerList[2].first,
            amount = beerList[2].second
        )
    }
}