package at.luki0606.beerney.viewModels.BeerActions

import BeerTable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import at.luki0606.beerney.models.BeerRepository

import java.lang.IllegalArgumentException


class BeerViewModel(private  val repository: BeerRepository) : ViewModel() {

    var beerList: LiveData<List<BeerTable>> = repository.getAllBeer.asLiveData()

}

class BeerModelFactory(private val repository: BeerRepository): ViewModelProvider.Factory{

    override  fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(BeerViewModel::class.java))
            return BeerViewModel(repository) as T
        throw  IllegalArgumentException("Unknown Class")
    }

}