package at.luki0606.beerney.models

import BeerDao
import BeerTable
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BeerRepository(private val beerDao: BeerDao) {

    val getAllBeer: Flow<List<BeerTable>> = beerDao.getAll()

    @WorkerThread
    suspend fun  insertBeer(beerTable: BeerTable){
        beerDao.insert(beerTable)
    }

    @WorkerThread
    suspend fun  updateBeer(beerTable: BeerTable){
        beerDao.update(beerTable)
    }
}