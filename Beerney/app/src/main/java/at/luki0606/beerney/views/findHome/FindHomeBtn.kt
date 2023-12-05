package at.luki0606.beerney.views.findHome

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import java.util.Locale

@Composable
fun FindHomeBtn(viewModel: FindHomeViewModel){
    val address by viewModel.address
    val context = LocalContext.current
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = address.isNotEmpty(),
        onClick = {
            geocoder.getFromLocationName(address, 1) { addresses ->
                if (addresses.isNotEmpty()) {
                    viewModel.updateTargetPositionAndBearing(
                        addresses[0].latitude,
                        addresses[0].longitude
                    )
                } else {
                    Toast.makeText(context, "Could not found location", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    ) {
        Text(text = "FIND HOME!",
            modifier = Modifier.padding(8.dp),
            color = Alabaster
        )
    }
}