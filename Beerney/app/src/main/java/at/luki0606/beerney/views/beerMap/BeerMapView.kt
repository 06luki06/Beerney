package at.luki0606.beerney.views.beerMap

import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

@Composable
fun BeerMapView(viewModel: BeerMapViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val currentLocationState = viewModel.currentLocation.collectAsState()
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val currentLocation = LatLng(currentLocationState.value.latitude, currentLocationState.value.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AddBeer(viewModel, currentLocation, geocoder, showDialog = showDialog){ closeDialog -> showDialog = closeDialog}
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            viewModel.beerList.value.forEach{ beer ->
                Marker(
                    state = MarkerState(position = LatLng(beer.latitude, beer.longitude)),
                    title = beer.brand,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                    snippet = beer.city,
                )
            }

            Marker(
                state = MarkerState(position = currentLocation),
                title = "Current Location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                snippet = "This is your current location",
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { showDialog = true},
        ) {
            Row {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add a beer",
                    tint = Alabaster
                )
                Spacer(modifier = Modifier
                    .width(8.dp))
                Text(text = "BEERNEY",
                    color = Alabaster)
            }
        }
    }
}