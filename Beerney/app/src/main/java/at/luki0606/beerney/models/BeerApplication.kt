package at.luki0606.beerney.models

import android.app.Application

class BeerApplication : Application() {

    private val database by lazy { BeerDatabase.getDatabase(this) }
    val repository by lazy { BeerRepository(database.beerDao()) }
}