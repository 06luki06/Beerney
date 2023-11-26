package at.luki0606.beerney.views

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import at.luki0606.beerney.models.BeerModel
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.services.CurrentLocationManager
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import at.luki0606.beerney.viewModels.beerMap.BeerMapViewModel
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import at.luki0606.beerney.views.beerList.BeerList
import at.luki0606.beerney.views.beerMap.BeerMap
import at.luki0606.beerney.views.findHome.FindHome
import at.luki0606.beerney.views.navigation.NavigationBar
import at.luki0606.beerney.views.statistics.Statistics
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Date
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

        // Test data
        BeerRepository.addBeer(BeerModel("Gösser",14.883664859187075,47.546196697500996,"Eisenerz", Date()))
        BeerRepository.addBeer(BeerModel("Gösser",  14.88450903189536,47.54573495514112,"Eisenerz", Date()))
        BeerRepository.addBeer(BeerModel("Gösser",  14.885435018449156, 47.54297212274162,"Eisenerz", Date()))
        BeerRepository.addBeer(BeerModel("Puntigamer",  14.883410332520038, 47.54476761059799,"Eisenerz", Date()))
        BeerRepository.addBeer(BeerModel("Puntigamer", 14.993305651806542, 47.487975838522765, "Vordernberg", Date()))
        BeerRepository.addBeer(BeerModel("Schwechater", 14.993875252470868, 47.487836716136655, "Vordernberg", Date()))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationManager = CurrentLocationManager(this, calculateCurrentPositionCallback)

        setContent {
            BeerneyTheme {
                BuildView(beerListViewModel, findHomeViewModel, beerMapViewModel)
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
fun BuildView(beerListViewModel: BeerListViewModel, findHomeViewModel: FindHomeViewModel, beerMapViewModel: BeerMapViewModel){
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
                0 -> BeerList(beerListViewModel)
                1 -> BeerMap(beerMapViewModel)
                2 -> Statistics()
                3 -> FindHome(findHomeViewModel)
                else -> BeerList(beerListViewModel)
            }
        }

        NavigationBar(selectedIndex){newIndex -> selectedIndex = newIndex}
    }
}
