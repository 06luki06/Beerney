package at.luki0606.beerney.views.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Ebony

@Composable
fun ShowStatistics() {
    Column {
        Text(
            text = "STATISTICS",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            color = Ebony
        )
        ShowSingleStatistic(
            key = "Total beers drunk",
            value = BeerRepository.getBeerCount().toString()
        )
        ShowSingleStatistic(
            key = "Beers drunk this week",
            value = BeerRepository.getBeersCountDrunkWithinCurrentWeek().toString()
        )
        ShowSingleStatistic(
            key = "Favorite location",
            value = BeerRepository.getFavoriteDrinkingLocation()
        )
        ShowSingleStatistic(
            key = "Avg. beer / day",
            value = ("%.2f".format(BeerRepository.getAvgBeerPerDay()))
        )
    }
}