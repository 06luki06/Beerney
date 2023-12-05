package at.luki0606.beerney.views.findHome

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import kotlin.math.absoluteValue

@Composable
fun FindHomeView(viewModel: FindHomeViewModel){
    val bearing by viewModel.bearing
    val currentLocation = viewModel.currentLocation.collectAsState()
    val address by viewModel.address
    val targetPosition by viewModel.targetPosition.collectAsState()

    val currentLoc = Location(LocationManager.GPS_PROVIDER)
    currentLoc.latitude = currentLocation.value.latitude
    currentLoc.longitude = currentLocation.value.longitude

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowLocationValue(key = "Current lat:", value = "${currentLoc.latitude}°")
        ShowLocationValue(key = "Current long:", value = "${currentLoc.longitude}°")
        ShowLocationValue(key = "Home lat:", value = "${targetPosition.latitude}°")
        ShowLocationValue(key = "Home long:", value = "${targetPosition.longitude}°")
        ShowLocationValue(key = "Bearing:", value = "${bearing.absoluteValue}°")
        EnterAddress(address = address, viewModel = viewModel)
        Spacer(modifier = Modifier.height(25.dp))
        FindHomeBtn(viewModel = viewModel)
        if(targetPosition.latitude != 0.0 && targetPosition.longitude != 0.0){
            ArrowComposable(bearing = bearing)
        }
    }
}