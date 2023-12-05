package at.luki0606.beerney.views.findHome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import at.luki0606.beerney.ui.theme.Ebony
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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