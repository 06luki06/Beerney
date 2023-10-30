package at.luki0606.beerney.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import at.luki0606.beerney.views.beerList.BeerList
import at.luki0606.beerney.views.beerMap.BeerMap
import at.luki0606.beerney.views.findHome.FindHome
import at.luki0606.beerney.views.navigation.NavigationBar
import at.luki0606.beerney.views.statistics.Statistics
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private val beerListViewModel: BeerListViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //https://www.youtube.com/watch?v=mwzKYIB9cQs - code from here
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        setContent {
            BeerneyTheme {
                BuildView(beerListViewModel, currentLocation)
            }
        }
    }

    private fun getCurrentLocation() {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location: Location? = task.result
                    print(location.toString())
                    if (location == null) {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                    } else {
                        currentLocation = location
                    }
                }
            } else {
                Toast.makeText(this, "Please enable your location service", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }

    private fun isLocationEnabled(): Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Companion.PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



@Composable
fun BuildView(beerListViewModel: BeerListViewModel, currentLocation: Location){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Alabaster),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var selectedIndex by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .weight(1f)
        ){
            when (selectedIndex) {
                0 -> BeerMap()
                1 -> BeerList(beerListViewModel)
                2 -> Statistics()
                3 -> FindHome(currentLocation)
                else -> BeerMap()
            }
        }

        NavigationBar(selectedIndex){newIndex -> selectedIndex = newIndex}
    }
}
