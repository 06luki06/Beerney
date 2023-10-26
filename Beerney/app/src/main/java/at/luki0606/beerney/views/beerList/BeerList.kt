package at.luki0606.beerney.views.beerList

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun Preview() {
    BeerList()
}

@Composable
fun BeerList(){
    Text(text = "BeerList")
}