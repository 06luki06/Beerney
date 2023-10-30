package at.luki0606.beerney.models

import android.location.Location

object CurrentLocation {
    private var currentLocation: Location = Location("")

    fun getCurrentLocation(): Location {
        return currentLocation
    }

    fun setCurrentLocation(location: Location) {
        currentLocation = location
    }
}