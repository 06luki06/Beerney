package at.luki0606.beerney.models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CurrentLocation {
    private val _location = MutableStateFlow(LocationData(0.0, 0.0))
    val location: StateFlow<LocationData> = _location

    fun updateLocation(latitude: Double, longitude: Double) {
        _location.value = LocationData(latitude, longitude)
    }

    fun getLatitude(): Double {
        return _location.value.latitude
    }

    fun getLongitude(): Double {
        return _location.value.longitude
    }
}

data class LocationData(val latitude: Double, val longitude: Double)
