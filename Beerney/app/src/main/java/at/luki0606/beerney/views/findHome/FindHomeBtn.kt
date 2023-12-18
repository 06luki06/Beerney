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
import androidx.lifecycle.viewModelScope
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FindHomeBtn(viewModel: FindHomeViewModel){
    val address by viewModel.address
    val context = LocalContext.current
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if(address.isEmpty()){
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Please enter an address", Toast.LENGTH_SHORT).show()
                }
            }else{
                geocoder.getFromLocationName(address, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        viewModel.updateTargetPositionAndBearing(
                            addresses[0].latitude,
                            addresses[0].longitude,
                        )
                        viewModel.viewModelScope.launch {
                            BeerRepository.updateHomingPosition(address)
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "Could not find location", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    ) {
        Text(text = "BEER ME HOME!",
            modifier = Modifier.padding(8.dp),
            color = Alabaster
        )
    }
}