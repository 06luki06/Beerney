package at.luki0606.beerney.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Build
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
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.services.CurrentLocationManager
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.BeerneyTheme
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
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
import kotlin.properties.Delegates

class MainActivity : ComponentActivity() {
    private val beerListViewModel: BeerListViewModel by viewModels()
    private val findHomeViewModel: FindHomeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation = CurrentLocation
    private lateinit var currentLocationManager: CurrentLocationManager
    private var arePermissionsEnabled by Delegates.notNull<Boolean>()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT < 33){
            Toast.makeText(this, "This app requires Android 12 or higher", Toast.LENGTH_LONG).show()
            finish()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationManager = CurrentLocationManager(this, calculateCurrentPositionCallback)

        setContent {
            BeerneyTheme {
                BuildView(beerListViewModel, findHomeViewModel)
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
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
fun BuildView(beerListViewModel: BeerListViewModel, findHomeViewModel: FindHomeViewModel){
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
                3 -> FindHome(findHomeViewModel)
                else -> BeerMap()
            }
        }

        NavigationBar(selectedIndex){newIndex -> selectedIndex = newIndex}
    }
}
