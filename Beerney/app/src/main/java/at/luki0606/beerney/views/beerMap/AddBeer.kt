package at.luki0606.beerney.views.beerMap

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import at.luki0606.beerney.models.BeerInfoSSPrefs.getBeerInfo
import at.luki0606.beerney.models.BeerInfoSSPrefs.setBeerInfo
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.util.Date
import java.util.Locale


@Composable
fun AddBeer(viewModel: BeerMapViewModel, currentLocation: LatLng, geocoder: Geocoder, showDialog: Boolean, onShowDialogChanged: (Boolean) -> Unit){
    var beerName by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onShowDialogChanged(false)
            },
            title = { Text(text = "Time for Beerney!") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = beerName,
                        onValueChange = {
                            beerName = it
                        },
                        label = { Text("Beer Brand") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        var geocoderCity: String
                        geocoder.getFromLocation(currentLocation.latitude,
                            currentLocation.longitude, 1){cities ->
                            geocoderCity = if(cities.isNotEmpty()){
                                cities[0].locality
                            } else {
                                "Unknown"
                            }

                            val brandUntrimmed = beerName
                            beerName = brandUntrimmed.trim()
                            beerName = beerName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else{
                                    it.toString()
                                }
                            }

                            val model = BeerModel(
                                id = -1,
                                brand = beerName,
                                longitude = currentLocation.longitude,
                                latitude = currentLocation.latitude,
                                city = geocoderCity,
                                drunkAt = Date()
                            )

                            viewModel.viewModelScope.launch {
                                val couldAddBeer = BeerRepository.addBeer(model)
                                try {
                                    if (!getBeerInfo(context)) {
                                        BeerRepository.getBeerInfo()
                                        setBeerInfo(context)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                withContext(Dispatchers.Main){
                                    if(couldAddBeer){
                                        Toast.makeText(viewModel.getApplication(), "Beer added", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(viewModel.getApplication(), "Error adding beer", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            beerName = ""
                        }
                        onShowDialogChanged(false)
                    }
                ) {
                    Text(text = "Add Beer",
                        color = Alabaster
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onShowDialogChanged(false)
                    }
                ) {
                    Text(text = "Cancel",
                        color = Alabaster
                    )
                }
            }
        )
    }
}