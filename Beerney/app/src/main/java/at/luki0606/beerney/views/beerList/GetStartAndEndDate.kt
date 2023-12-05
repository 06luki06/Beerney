package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetStartAndEndDate(
    beerListViewModel: BeerListViewModel,
) {
    val dateTime = LocalDateTime.now()

    val initStartDate by beerListViewModel.selectedStartDate
    val initEndDate by beerListViewModel.selectedEndDate


    val dateRangePickerState = remember {
        DateRangePickerState(
            initialSelectedStartDateMillis = initStartDate,
            initialDisplayedMonthMillis = null,
            initialSelectedEndDateMillis = initEndDate,
            initialDisplayMode = DisplayMode.Input,
            yearRange = (dateTime.year - 10)..(dateTime.year + 10)
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .background(Ebony),
            title = null,
            headline = null,
        )

        val selectedStartDate = dateRangePickerState.selectedStartDateMillis
        val selectedEndDate = dateRangePickerState.selectedEndDateMillis

        DisposableEffect(selectedStartDate, selectedEndDate) {
            if (selectedStartDate != null) {
                beerListViewModel.setStartDate(selectedStartDate)
            }
            if (selectedEndDate != null) {
                beerListViewModel.setEndDate(selectedEndDate)
            }
            onDispose { }
        }
    }
}