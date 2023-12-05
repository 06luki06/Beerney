package at.luki0606.beerney.models

import android.content.Context
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

object BeerRepository {
    private var beers = mutableListOf<BeerModel>()

    suspend fun addBeer(beer: BeerModel, context: Context) = withContext(Dispatchers.IO){
        val jsonBody = Gson().toJson(beer)

        Fuel.post("http://${IpAddress.getIpAddress(context)}:3000/beers")
            .header("Content-Type" to "application/json")
            .body(jsonBody)
            .response{_, _, result ->
                when(result){
                    is Result.Success -> {
                        println("Beer added")
                    }
                    is Result.Failure -> {
                        println("Error: ${result.getException()}")
                    }
                    else -> {
                        println("Unknown error")
                    }
                }
            }
    }

    suspend fun deleteBeer(beerId: Int, context: Context) = withContext(Dispatchers.IO){
        val(_, _, result) = Fuel.delete("http://${IpAddress.getIpAddress(context)}:3000/beers/$beerId")
            .responseString()

        when (result){
            is Result.Success -> {
                println("Beer deleted")
            }
            is Result.Failure -> {
                println("Error has happened")
            }
            else -> {
                println("Unknown error")
            }
        }
    }

    suspend fun fetchBeers(context: Context) = withContext(Dispatchers.IO){
        val(_, _, result) = Fuel.get("http://${IpAddress.getIpAddress(context)}:3000/beers")
            .responseString()

        when (result){
            is Result.Success -> {
                val jsonString = result.get()
                beers = Gson().fromJson(jsonString, Array<BeerModel>::class.java).toMutableList()
            }
            is Result.Failure -> {
                println("Error: ${result.getException()}")
            }
            else -> {
                println("Unknown error")
            }
        }
    }

    fun getBeers(): List<BeerModel> {
        return beers
    }

    fun getBeer(beerId: Int): BeerModel{
        return beers[beerId]
    }

    fun getBeerCount(): Int{
        return beers.size
    }

    fun getBeerCount(beerBrand: String): Int{
        return beers.filter { it.brand == beerBrand }.size
    }

    fun getFavoriteDrinkingLocation(): String {
        val cityOccurrences = mutableMapOf<String, Int>()

        for (beer in beers) {
            val city = beer.city
            cityOccurrences[city] = (cityOccurrences[city] ?: 0) + 1
        }

        val favoriteCity = cityOccurrences.maxByOrNull { it.value }?.key

        return favoriteCity ?: ""
    }


    fun getBeersSortedByBeerCount(): List<Pair<String, Int>> {
        val beerCounts = mutableMapOf<String, Int>()

        for (beer in beers) {
            val brand = beer.brand
            beerCounts[brand] = (beerCounts[brand] ?: 0) + 1
        }

        if (beerCounts.size < 3) {
            for (i in beerCounts.size..2) {
                beerCounts["empty$i"] = 0
            }
        }
        return beerCounts.toList().sortedByDescending { (_, value) -> value }
    }

    fun getBeersCountDrunkWithinCurrentWeek(): Int {
        val calendar = Calendar.getInstance()

        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.time = Date()
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val startOfWeek = calendar.timeInMillis

        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999

        val endOfWeek = calendar.timeInMillis

        val beersWithinWeek = beers.count { beer ->
            val beerTime = beer.drunkAt.time
            beerTime in startOfWeek..endOfWeek
        }

        return beersWithinWeek
    }

    fun getAvgBeerPerDay(): Double {
        if (beers.isEmpty()) {
            return 0.0
        }

        val today = Calendar.getInstance()
        today[Calendar.HOUR_OF_DAY] = 23
        today[Calendar.MINUTE] = 59
        today[Calendar.SECOND] = 59
        today[Calendar.MILLISECOND] = 999

        val firstBeerDate = beers.minByOrNull { it.drunkAt }?.drunkAt ?: return 0.0

        val daysBetween = ((today.timeInMillis - firstBeerDate.time) / (24 * 60 * 60 * 1000)).toFloat()

        val totalBeers = beers.size.toFloat()
        val avgBeersPerDay = totalBeers / daysBetween

        return avgBeersPerDay.toDouble()
    }

    fun getBeerBrands(): Array<String>{
        val beerBrands: MutableList<String> = mutableListOf()
        beerBrands.add("All")
        for(beer in beers){
            if(!beerBrands.contains(beer.brand)){
                beerBrands.add(beer.brand)
            }
        }
        return beerBrands.toTypedArray()
    }

}