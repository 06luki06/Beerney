package at.luki0606.beerney.views.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.luki0606.beerney.ui.theme.Bronze
import at.luki0606.beerney.ui.theme.Gold
import at.luki0606.beerney.ui.theme.Silver

@Composable
fun PodiumColumn(rank: Int, brand: String, amount: Int) {
    var brandIntern = brand
    var amountIntern = amount.toString()

    if(brand.contains("empty") && amount == 0){
        brandIntern = ""
        amountIntern = ""
    }

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
            text = brandIntern,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
        )
        Text(
            text = amountIntern,
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