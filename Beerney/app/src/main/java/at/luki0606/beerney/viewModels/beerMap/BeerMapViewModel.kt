package at.luki0606.beerney.viewModels.beerMap

import androidx.lifecycle.ViewModel
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.LocationData
import kotlinx.coroutines.flow.StateFlow

class BeerMapViewModel: ViewModel() {
    private val _currentLocation = CurrentLocation.location
    val currentLocation: StateFlow<LocationData> = _currentLocation
}