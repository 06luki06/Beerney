package at.luki0606.beerney.views.statistics

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.Bronze
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.ui.theme.Gold
import at.luki0606.beerney.ui.theme.Silver

@Composable
fun Statistics(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ShowPodium()
        Spacer(modifier = Modifier.height(50.dp))
        ShowStatistics()
        Spacer(modifier = Modifier.height(25.dp))
        ShareStats()
    }
}

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

@Composable
fun PodiumColumn(rank: Int, brand: String, amount: Int) {
    val boxHeight = when (rank) {
        1 -> 150.dp
        2 -> 100.dp
        3 -> 50.dp
        else -> 50.dp
    }

    val color = when (rank) {
        1 -> Gold
        2 -> Silver
        3 -> Bronze
        else -> Color.Gray
    }

    val cornerShape = when (rank) {
        1 -> RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
        2 -> RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
        3 -> RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
        else -> RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            // should be relative to screen width
            .width(120.dp)
    ) {
        Text(
            text = brand,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
        )
        Text(
            text = amount.toString(),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
        )
        Box(
            modifier = Modifier
                .height(boxHeight)
                .fillMaxWidth()
                .background(color, cornerShape)
        ) {
            Text(
                text = rank.toString(),
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
@Composable
fun ShowStatistics(){
    Column {
        Text(text = "STATISTICS",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            color = Ebony
        )
        ShowSingleStatistic(key = "Total beers drunk", value = BeerRepository.getBeerCount().toString())
        ShowSingleStatistic(key = "Beers drunk this week", value = BeerRepository.getBeersCountDrunkWithinCurrentWeek().toString())
        ShowSingleStatistic(key = "Favorite location", value = BeerRepository.getFavoriteDrinkingLocation())
        //shows infinity
        ShowSingleStatistic(key = "Avg. beer / day", value = BeerRepository.getAvgBeerPerDay().toString())
    }
}

@Composable
fun ShowSingleStatistic(key: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = key,
            color = Ebony,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Text(text= value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End)
    }
}

@Composable
fun ShareStats(){
    val shareText by remember { mutableStateOf(TextFieldValue("Check out my Beerney stats:\n\nI have drunk ${BeerRepository.getBeersCountDrunkWithinCurrentWeek()}  \uD83C\uDF7B this week!")) }
    val context = LocalContext.current
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Shared data successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Sharing failed", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText.text)
                    putExtra(Intent.EXTRA_SUBJECT, "Beerney stats")
                    putExtra(Intent.EXTRA_TITLE, "Check out my Beerney stats!")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share Beerney stats")
                shareLauncher.launch(shareIntent)
            }
            .background(Ebony, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = "Share stats",
                tint = Alabaster)
            Text(
                text = "Share your stats!",
                color = Alabaster
            )
        }
    }
}