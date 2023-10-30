package at.luki0606.beerney.models

object CurrentLocation {
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    fun getLatitude(): Double {
        return currentLatitude
    }

    fun setLatitude(latitude: Double) {
        currentLatitude = latitude
    }

    fun getLongitude(): Double {
        return currentLongitude
    }

    fun setLongitude(longitude: Double) {
        currentLongitude = longitude
    }
}