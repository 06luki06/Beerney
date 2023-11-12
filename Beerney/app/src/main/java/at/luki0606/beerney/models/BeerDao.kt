import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BeerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beerTable: BeerTable)

    @Update
    suspend fun update(beerTable: BeerTable)

    @Query("SELECT * FROM beer_Table ORDER BY id DESC")
    fun getAll(): Flow<List<BeerTable>>

    @Delete
    fun delete(beer: BeerTable )

    @Query("SELECT *  FROM beer_Table WHERE id Like :id" )
    fun getBeer(id: Int) : BeerTable
}

