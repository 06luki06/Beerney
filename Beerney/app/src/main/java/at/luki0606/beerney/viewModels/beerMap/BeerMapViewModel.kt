package at.luki0606.beerney.viewModels.beerMap

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.LocationData
import kotlinx.coroutines.flow.StateFlow

class BeerMapViewModel(application: Application): AndroidViewModel(application) {
    private val _currentLocation = CurrentLocation.location
    val currentLocation: StateFlow<LocationData> = _currentLocation

    private val _beerList = mutableStateOf(BeerRepository.getBeers())
    val beerList: State<List<BeerModel>> = _beerList

    fun updateBeerList(){
        _beerList.value = BeerRepository.getBeers()
    }
}