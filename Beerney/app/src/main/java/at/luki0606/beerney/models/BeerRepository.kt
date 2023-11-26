package at.luki0606.beerney.models

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