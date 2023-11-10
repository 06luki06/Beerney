package at.luki0606.beerney.views.findHome

import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.EarthYellow
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun FindHome(viewModel: FindHomeViewModel){
    val bearing by viewModel.bearing
    val currentLocation = viewModel.currentLocation.collectAsState()
    val address by viewModel.address
    val targetPosition by viewModel.targetPosition.collectAsState()

    val currentLoc = Location(LocationManager.GPS_PROVIDER)
    currentLoc.latitude = currentLocation.value.latitude
    currentLoc.longitude = currentLocation.value.longitude

    //TODO: add rotation sensor to update bearing - do we want to do this?

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowLocationValue(key = "Current lat:", value = "${currentLoc.latitude}°")
        ShowLocationValue(key = "Current long:", value = "${currentLoc.longitude}°")
        ShowLocationValue(key = "Home lat:", value = "${targetPosition.latitude}°")
        ShowLocationValue(key = "Home long:", value = "${targetPosition.longitude}°")
        ShowLocationValue(key = "Bearing:", value = "${bearing.absoluteValue}°")
        EnterAddress(address = address, viewModel = viewModel)
        Spacer(modifier = Modifier.height(25.dp))
        FindHomeBtn(viewModel = viewModel)
        if(targetPosition.latitude != 0.0 && targetPosition.longitude != 0.0){
            ArrowComposable(bearing = bearing)
        }
    }
}

@Composable
fun ArrowComposable(bearing: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val centerX = canvasWidth / 2
            val centerY = canvasHeight / 2

            val arrowLength = min(canvasWidth, canvasHeight) * 0.5f
            val arrowWidth = 15f
            val arrowColor = Ebony

            val arrowRotation = Math.toRadians(90.0 - bearing.toDouble()).toFloat()

            drawLine(
                color = arrowColor,
                start = Offset(centerX, centerY),
                end = Offset(
                    centerX + arrowLength * cos(arrowRotation),
                    centerY - arrowLength * sin(arrowRotation)
                ),
                strokeWidth = arrowWidth
            )

            val headLength = arrowLength * 0.2f
            val headOffsetX = centerX + arrowLength * cos(arrowRotation)
            val headOffsetY = centerY - arrowLength * sin(arrowRotation)

            val headBase = Offset(
                headOffsetX + headLength * cos(arrowRotation + Math.toRadians(165.0).toFloat()),
                headOffsetY - headLength * sin(arrowRotation + Math.toRadians(165.0).toFloat())
            )

            val headTip = Offset(
                headOffsetX + headLength * cos(arrowRotation - Math.toRadians(165.0).toFloat()),
                headOffsetY - headLength * sin(arrowRotation - Math.toRadians(165.0).toFloat())
            )

            drawPath(
                path = Path().apply {
                    moveTo(headOffsetX, headOffsetY)
                    lineTo(headBase.x, headBase.y)
                    lineTo(headTip.x, headTip.y)
                    close()
                },
                color = arrowColor
            )
        }
    }
}

@Composable
fun EnterAddress(address: String, viewModel: FindHomeViewModel){
    OutlinedTextField(
        value = address,
        label = { Text("Address") },
        modifier = Modifier.fillMaxWidth()
            .padding(bottom=8.dp),
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.Place, contentDescription = "Home-Address") },
        placeholder = { Text("Enter address") },
        onValueChange = { newAddress -> viewModel.setCity(newAddress) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Ebony,
            unfocusedTextColor = Ebony,
            cursorColor = Ebony,
            focusedContainerColor = Alabaster,
            unfocusedContainerColor = Alabaster,
            focusedLeadingIconColor = Ebony,
            unfocusedLeadingIconColor = EarthYellow,
            focusedLabelColor = Ebony,
            unfocusedLabelColor = EarthYellow,
        )
    )
}

@Composable
fun FindHomeBtn(viewModel: FindHomeViewModel){
    val address by viewModel.address
    val context = LocalContext.current
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = address.isNotEmpty(),
        onClick = {
            if (Build.VERSION.SDK_INT >= 33) {
                geocoder.getFromLocationName(address, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        viewModel.updateTargetPositionAndBearing(
                            addresses[0].latitude,
                            addresses[0].longitude
                        )
                    } else {
                        Toast.makeText(context, "Could not found location", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    ) {
        Text(text = "FIND HOME!",
            modifier = Modifier.padding(8.dp),
            color = Alabaster
        )
    }
}

@Composable
fun ShowLocationValue(key: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = key,
            color = Ebony,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Text(text= value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End)
    }
}