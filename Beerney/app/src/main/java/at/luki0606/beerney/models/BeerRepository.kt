package at.luki0606.beerney.models

import java.util.Calendar
import java.util.Date

object BeerRepository {
    private val beers = mutableListOf<BeerModel>()

    fun addBeer(beer: BeerModel){
        beers.add(beer)
    }

    fun getBeers(): List<BeerModel>{
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

    fun deleteBeer(beer: BeerModel){
        beers.remove(beer)
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