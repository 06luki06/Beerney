package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.DarkRed
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GetBeerList(viewModel: BeerListViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
    ){
        itemsIndexed(viewModel.filteredBeerList.value) { _, beer ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(text = beer.brand,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.width(200.dp))
                Column {
                    Text(text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(beer.drunkAt),
                        style = MaterialTheme.typography.bodySmall)
                    Text(text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(beer.drunkAt) + " h",
                        style = MaterialTheme.typography.bodySmall)
                    Text(text = beer.city,
                        style = MaterialTheme.typography.bodySmall)
                }
                IconButton(
                    onClick = {
                        viewModel.deleteBeer(beer)
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .background(DarkRed, RoundedCornerShape(4.dp))
                ) {
                    Icon(imageVector = Icons.Default.Delete,
                        tint = Alabaster,
                        contentDescription = "Delete")
                }
            }
            Divider()
        }
    }
}