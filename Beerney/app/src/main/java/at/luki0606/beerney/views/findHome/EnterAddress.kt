package at.luki0606.beerney.views.findHome

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.EarthYellow
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel

@Composable
fun EnterAddress(address: String, viewModel: FindHomeViewModel){
    OutlinedTextField(
        value = address,
        label = { Text("Address") },
        modifier = Modifier.fillMaxWidth()
            .padding(bottom=8.dp),
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.Place, contentDescription = "Home-Address") },
        placeholder = { Text("Enter address") },
        onValueChange = { newAddress -> viewModel.setAddress(newAddress) },
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
        )
    )
}