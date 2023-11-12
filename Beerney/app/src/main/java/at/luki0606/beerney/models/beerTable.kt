import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "beer_Table")
data class BeerTable(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name= "beer_brand") val brand: String,
        @ColumnInfo(name= "latitude")val lat: Double,
        @ColumnInfo(name= "longitude")val lon: Double,
        @ColumnInfo(name= "city")var city: String,
        @ColumnInfo(name= "timestamp")val timestamp: String?
)


