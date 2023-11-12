package at.luki0606.beerney.views.beerMap

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
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun BeerMap(viewModel: BeerMapViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val currentLocationState = viewModel.currentLocation.collectAsState()
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
        AddBeer(showDialog = showDialog){ closeDialog -> showDialog = closeDialog}
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            Marker(
                state = MarkerState(position = currentLocation),
                title = "Current Location",
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
fun AddBeer(showDialog: Boolean, onShowDialogChanged: (Boolean) -> Unit){
    val context = LocalContext.current
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