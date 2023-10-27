package at.luki0606.beerney.viewModels.beerList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import at.luki0606.beerney.views.beerList.toMillis
import java.time.LocalDateTime

class BeerListViewModel : ViewModel() {
    private val _city = mutableStateOf("")
    val city: State<String> = _city

    private val _selectedBrand = mutableStateOf("")
    val selectedBrand: State<String> = _selectedBrand

    //change it to first entry of repository
    private val _selectedStartDate = mutableLongStateOf(LocalDateTime.now().toMillis())
    val selectedStartDate: State<Long> = _selectedStartDate

    private val _selectedEndDate = mutableLongStateOf(LocalDateTime.now().toMillis())
    val selectedEndDate: State<Long> = _selectedEndDate

    fun setCity(newCity: String) {
        _city.value = newCity
    }

    fun setBrand(newBrand: String) {
        _selectedBrand.value = newBrand
    }

    fun setStartDate(newStartDate: Long) {
        _selectedStartDate.longValue = newStartDate
    }

    fun setEndDate(newEndDate: Long) {
        _selectedEndDate.longValue = newEndDate
    }
}