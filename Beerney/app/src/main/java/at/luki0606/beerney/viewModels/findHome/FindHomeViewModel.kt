package at.luki0606.beerney.viewModels.findHome

import android.location.Location
import android.location.LocationManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FindHomeViewModel : ViewModel() {
    private val _currentLocation = CurrentLocation.location
    val currentLocation: StateFlow<LocationData> = _currentLocation

    private val _targetAddress = mutableStateOf("")
    val address: State<String> = _targetAddress

    private val _targetPosition = MutableStateFlow(LocationData(0.0, 0.0))
    val targetPosition: StateFlow<LocationData> = _targetPosition

    private val _bearing = mutableFloatStateOf(0f)
    val bearing: State<Float> = _bearing

    fun setCity(newAddress: String) {
        _targetAddress.value = newAddress
    }

    fun updateTargetPositionAndBearing(latitude: Double, longitude: Double) {
        _targetPosition.value = LocationData(latitude, longitude)
        updateBearing(locationDataToLocation(_currentLocation.value).bearingTo(locationDataToLocation(_targetPosition.value)))
    }

    private fun updateBearing(newBearing: Float){
        _bearing.floatValue = newBearing
    }

    private fun locationDataToLocation(locationData: LocationData): Location {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = locationData.latitude
        location.longitude = locationData.longitude
        return location
    }
}