package at.luki0606.beerney.viewModels.beerList

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.views.beerList.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BeerListViewModel(application: Application) : AndroidViewModel(application) {
    private val _city = mutableStateOf("")
    val city: State<String> = _city

    private val _selectedBrand = mutableStateOf("")
    val selectedBrand: State<String> = _selectedBrand

    private val _selectedStartDate = mutableLongStateOf(LocalDateTime.now().toMillis())
    val selectedStartDate: State<Long> = _selectedStartDate

    private val _selectedEndDate = mutableLongStateOf(LocalDateTime.now().toMillis())
    val selectedEndDate: State<Long> = _selectedEndDate

    private val _beerList = mutableStateOf<List<BeerModel>>(emptyList())
    private val _filteredBeerList = mutableStateOf<List<BeerModel>>(emptyList())
    val filteredBeerList: State<List<BeerModel>> = _filteredBeerList

    init{
        updateBeerListAsync(application.applicationContext)
    }

    private fun updateBeerListAsync(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val beers = BeerRepository.getBeers(context)
                _beerList.value = beers
                _filteredBeerList.value = beers
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }

    fun setCity(newCity: String) {
        _city.value = newCity
        updateFilteredBeerList()
    }

    fun setBrand(newBrand: String) {
        _selectedBrand.value = newBrand
        updateFilteredBeerList()
    }

    fun setStartDate(newStartDate: Long) {
        _selectedStartDate.longValue = newStartDate
        updateFilteredBeerList()
    }

    fun setEndDate(newEndDate: Long) {
        _selectedEndDate.longValue = newEndDate
        updateFilteredBeerList()
    }

    fun deleteBeer(beer: BeerModel, context: Context){
        BeerRepository.deleteBeer(beer)
        updateBeerListAsync(context)
        updateFilteredBeerList()
    }

    fun updateFilteredBeerList() {
        val brandFilter = _selectedBrand.value
        _filteredBeerList.value = if (brandFilter.isEmpty() || brandFilter == "All") {
            _beerList.value
        } else {
            _beerList.value.filter { it.brand == brandFilter }
        }

        val cityFilter = _city.value
        _filteredBeerList.value = if (cityFilter.isEmpty()) {
            _filteredBeerList.value
        } else {
            _filteredBeerList.value.filter { it.city == cityFilter }
        }

        val startDateFilter = _selectedStartDate.longValue
        _filteredBeerList.value = if (startDateFilter == 0L) {
            _filteredBeerList.value
        } else {
            val adjustedStartDate = startDateFilter - (24 * 60 * 60 * 1000)
            _filteredBeerList.value.filter { it.drunkAt.time >= adjustedStartDate }
        }

        val endDateFilter = _selectedEndDate.longValue
        _filteredBeerList.value = if (endDateFilter == 0L) {
            _filteredBeerList.value
        } else {
            val adjustedEndDate = endDateFilter + (24 * 60 * 60 * 1000)
            _filteredBeerList.value.filter { it.drunkAt.time <= adjustedEndDate }
        }
    }
}
