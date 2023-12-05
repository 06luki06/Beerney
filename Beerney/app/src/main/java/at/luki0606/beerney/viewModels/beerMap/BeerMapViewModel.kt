package at.luki0606.beerney.viewModels.beerMap

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.LocationData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BeerMapViewModel(application: Application): AndroidViewModel(application) {
    private val _currentLocation = CurrentLocation.location
    val currentLocation: StateFlow<LocationData> = _currentLocation

    private val _beerList = mutableStateOf<List<BeerModel>>(emptyList())
    val beerList: State<List<BeerModel>> = _beerList
    init{
        updateBeerListAsync(application.applicationContext)
    }

    private fun updateBeerListAsync(context: Context) {
        viewModelScope.launch {
            try {
                val beers = BeerRepository.getBeers(context)
                _beerList.value = beers
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }
}