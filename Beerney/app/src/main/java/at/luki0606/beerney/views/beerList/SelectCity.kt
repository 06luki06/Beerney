package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.EarthYellow
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel

@Composable
fun SelectCity(beerListViewModel : BeerListViewModel){
    val city by beerListViewModel.city
    OutlinedTextField(
        value = city,
        label = { Text("City") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = "Location") },
        placeholder = { Text("Enter city") },
        onValueChange = { newCity -> beerListViewModel.setCity(newCity) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Ebony,
            unfocusedTextColor = Ebony,
            cursorColor = Ebony,
            focusedContainerColor = Alabaster,
            unfocusedContainerColor = Alabaster,
            focusedLeadingIconColor = Ebony,
            unfocusedLeadingIconColor = EarthYellow,
            focusedLabelColor = Ebony,
            unfocusedLabelColor = EarthYellow,
        ),
    )
}