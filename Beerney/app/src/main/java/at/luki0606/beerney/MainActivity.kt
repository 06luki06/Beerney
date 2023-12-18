package at.luki0606.beerney

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.lifecycleScope
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.models.EstablishConnection
import at.luki0606.beerney.services.CurrentLocationManager
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import at.luki0606.beerney.views.beerList.BeerListView
import at.luki0606.beerney.views.beerMap.BeerMapView
import at.luki0606.beerney.views.findHome.FindHomeView
import at.luki0606.beerney.views.navigation.NavigationBarView
import at.luki0606.beerney.views.statistics.StatisticsView
import com.github.kittinunf.fuel.Fuel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class MainActivity : ComponentActivity() {
    private val beerListViewModel: BeerListViewModel by viewModels()
    private val findHomeViewModel: FindHomeViewModel by viewModels()
    private val beerMapViewModel: BeerMapViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation = CurrentLocation
    private lateinit var currentLocationManager: CurrentLocationManager
    private var arePermissionsEnabled by Delegates.notNull<Boolean>()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BeerRepository.initialize(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationManager = CurrentLocationManager(this, calculateCurrentPositionCallback)

        val ip = getString(R.string.ip_address)

        lifecycleScope.launch(Dispatchers.IO){
            try {
                val (_, response, _) = Fuel.get("${ip}/status")
                    .responseString()
                EstablishConnection.getConnection()
                withContext(Dispatchers.Main) {
                    if (response.statusCode == 200) {
                        Toast.makeText(this@MainActivity, "Connected to server", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Could not connect to server", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@MainActivity, "Make sure you are within the FH-Joanneum network", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                println("Error: $e")
            }
        }

        setContent {
            BeerneyTheme {
                BuildView(this@MainActivity, beerListViewModel, findHomeViewModel, beerMapViewModel)
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
                if (isLocationEnabled(getSystemService(LOCATION_SERVICE) as LocationManager)) {
                    arePermissionsEnabled = true
                    currentLocationManager.startLocationUpdates(this)
                } else {
                    Toast.makeText(this, "GPS is not enabled", Toast.LENGTH_SHORT).show()
                    arePermissionsEnabled = false
                    val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(settingsIntent)
                }
            }
            else -> {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
                arePermissionsEnabled = false
            }
        }
    }

    private val calculateCurrentPositionCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let { location ->
                currentLocation.updateLocation(location.latitude, location.longitude)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onPause() {
        super.onPause()
        if (arePermissionsEnabled) {
            currentLocationManager.stopLocationUpdates()
        }
    }
}

@Composable
fun BuildView(context: Context, beerListViewModel: BeerListViewModel, findHomeViewModel: FindHomeViewModel, beerMapViewModel: BeerMapViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Alabaster),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var selectedIndex by remember { mutableIntStateOf(0) }

        LaunchedEffect(selectedIndex){
            val couldFetch = BeerRepository.fetchBeers()
            if(couldFetch){
                beerMapViewModel.updateBeerList()
                beerListViewModel.updateBeerList()
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Could not fetch beers", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
        ){
            when (selectedIndex) {
                0 -> BeerListView(beerListViewModel)
                1 -> BeerMapView(beerMapViewModel)
                2 -> StatisticsView()
                3 -> FindHomeView(findHomeViewModel)
                else -> BeerListView(beerListViewModel)
            }
        }

        NavigationBarView(selectedIndex){
            newIndex -> selectedIndex = newIndex
        }
    }
}
