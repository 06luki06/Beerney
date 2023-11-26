package at.luki0606.beerney.viewModels.beerMap

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.LocationData
import kotlinx.coroutines.flow.StateFlow

class BeerMapViewModel: ViewModel() {
    private val _currentLocation = CurrentLocation.location
    val currentLocation: StateFlow<LocationData> = _currentLocation

    private val _beerList = mutableStateOf(BeerRepository.getBeers())
    val beerList: State<List<BeerModel>> = _beerList
}