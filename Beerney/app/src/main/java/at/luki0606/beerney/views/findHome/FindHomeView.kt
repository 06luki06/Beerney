package at.luki0606.beerney.views.findHome

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import at.luki0606.beerney.models.CurrentLocation
import at.luki0606.beerney.ui.theme.Ebony
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun FindHome(){
    var bearing by remember { mutableFloatStateOf(0f) }
    val currentLocation = CurrentLocation

    //create ViewModel to store and calculate everything

    val currentLoc = Location(LocationManager.GPS_PROVIDER)
    currentLoc.latitude = currentLocation.getLatitude()
    currentLoc.longitude = currentLocation.getLongitude()

    //set target position using address in TextField-> reverse geocoding
    val targetPosition = Location(LocationManager.GPS_PROVIDER)
    targetPosition.latitude = 47.525709
    targetPosition.longitude = 14.886129

    bearing = currentLoc.bearingTo(targetPosition)

    //TODO: add rotation sensor to update bearing

    Text(text = bearing.absoluteValue.toString())

    ArrowComposable(bearing = bearing)
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

            val arrowLength = min(canvasWidth, canvasHeight) * 0.4f
            val arrowWidth = 20f
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