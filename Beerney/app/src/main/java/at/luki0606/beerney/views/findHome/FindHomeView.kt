package at.luki0606.beerney.views.findHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.viewModels.findHome.FindHomeViewModel

@Composable
fun FindHomeView(viewModel: FindHomeViewModel){
    val bearing by viewModel.bearing
    val targetPosition by viewModel.targetPosition.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EnterAddress(viewModel = viewModel)
        FindHomeBtn(viewModel = viewModel)
        if(targetPosition.latitude != 0.0 && targetPosition.longitude != 0.0){
            ArrowComposable(bearing = bearing)
        }
    }
}