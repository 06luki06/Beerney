package at.luki0606.beerney.models

import java.util.Date

data class BeerModel(
    val brand: String,
    val longitude: Double,
    val latitude: Double,
    val city: String,
    val drunkAt: Date
)