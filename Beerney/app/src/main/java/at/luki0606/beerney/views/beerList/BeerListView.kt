@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel

@Composable
fun BeerListView(beerListViewModel: BeerListViewModel){
    beerListViewModel.updateFilteredBeerList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        SelectBeerBrand(beerListViewModel)
        SelectCity(beerListViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        GetStartAndEndDate(
            beerListViewModel
        )
        Spacer(modifier = Modifier.height(16.dp))
        GetBeerList(beerListViewModel)
    }
}