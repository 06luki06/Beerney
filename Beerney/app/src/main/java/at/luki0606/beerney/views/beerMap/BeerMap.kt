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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.lifecycle.viewModelScope
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@Composable
fun BeerMap(viewModel: BeerMapViewModel) {
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

@Composable
fun AddBeer(viewModel: BeerMapViewModel, currentLocation: LatLng, geocoder: Geocoder, showDialog: Boolean, onShowDialogChanged: (Boolean) -> Unit){
    var beerName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onShowDialogChanged(false)
            },
            title = { Text(text = "Time for Beerney!") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = beerName,
                        onValueChange = {
                            beerName = it
                        },
                        label = { Text("Beer Brand") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        var geocoderCity: String
                        geocoder.getFromLocation(currentLocation.latitude,
                            currentLocation.longitude, 1){cities ->
                            geocoderCity = if(cities.isNotEmpty()){
                                cities[0].locality
                            } else {
                                "Unknown"
                            }

                            val brandUntrimmed = beerName
                            beerName = brandUntrimmed.trim()
                            
                            val model = BeerModel(
                                id = 1,
                                brand = beerName,
                                longitude = currentLocation.longitude,
                                latitude = currentLocation.latitude,
                                city = geocoderCity,
                                drunkAt = Date())

                            viewModel.viewModelScope.launch {
                                BeerRepository.addBeer(model, viewModel.getApplication())
                            }
                        }
                        onShowDialogChanged(false)
                    }
                ) {
                    Text(text = "Add Beer",
                        color = Alabaster)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onShowDialogChanged(false)
                    }
                ) {
                    Text(text = "Cancel",
                        color = Alabaster)
                }
            }
        )
    }
}