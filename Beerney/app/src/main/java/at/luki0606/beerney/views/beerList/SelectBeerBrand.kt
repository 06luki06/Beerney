package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBeerBrand(beerListViewModel : BeerListViewModel) {
    val selectedBrand by beerListViewModel.selectedBrand
    var isExpanded by remember { mutableStateOf(false) }
    val availableBrands = BeerRepository.getBeerBrands()

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ){
        TextField(
            value = selectedBrand.ifEmpty {
                availableBrands[0]
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            modifier = Modifier.background(Ebony),
            onDismissRequest = { isExpanded = false}
        ) {
            availableBrands.forEach { brand ->
                DropdownMenuItem(
                    text = { Text(
                        text = brand,
                        color = Alabaster
                    ) },
                    onClick = {
                        isExpanded = false
                        beerListViewModel.setBrand(brand)
                    }
                )
            }
        }
    }
}