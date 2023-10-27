package at.luki0606.beerney.viewModels.beerList

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import at.luki0606.beerney.views.beerList.toMillis
import java.time.LocalDateTime

class BeerListViewModel : ViewModel() {
    private val _city = mutableStateOf("")
    val city: State<String> = _city
    var selectedBrand by mutableStateOf("")
    var selectedStartDate by mutableLongStateOf(LocalDateTime.now().toMillis())
    var selectedEndDate by mutableLongStateOf(LocalDateTime.now().toMillis())

    fun setCity(newCity: String) {
        _city.value = newCity
    }

}