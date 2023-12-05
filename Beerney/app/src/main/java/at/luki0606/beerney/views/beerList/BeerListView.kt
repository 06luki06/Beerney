@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package at.luki0606.beerney.views.beerList

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.DarkRed
import at.luki0606.beerney.ui.theme.EarthYellow
import at.luki0606.beerney.ui.theme.Ebony
import at.luki0606.beerney.viewModels.beerList.BeerListViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@Composable
fun BeerList(beerListViewModel: BeerListViewModel){
    beerListViewModel.updateFilteredBeerList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        SelectBeerBrand(beerListViewModel)
        SelectCity(beerListViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        GetStartAndEndDate(
            beerListViewModel
        )
        Spacer(modifier = Modifier.height(16.dp))
        GetBeerList(beerListViewModel)
    }
}


@Composable
fun SelectBeerBrand(beerListViewModel : BeerListViewModel) {
    val selectedBrand by beerListViewModel.selectedBrand
    var isExpanded by remember { mutableStateOf(false) }
    val availableBrands = BeerRepository.getBeerBrands()

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ){
        TextField(
            value = selectedBrand.ifEmpty {
                availableBrands[0]
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            modifier = Modifier.background(Ebony),
            onDismissRequest = { isExpanded = false}
        ) {
            availableBrands.forEach { brand ->
                DropdownMenuItem(
                    text = { Text(
                        text = brand,
                        color = Alabaster) },
                    onClick = {
                        isExpanded = false
                        beerListViewModel.setBrand(brand)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectCity(beerListViewModel : BeerListViewModel){
    val city by beerListViewModel.city
    OutlinedTextField(
        value = city,
        label = { Text("City") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = "Location") },
        placeholder = { Text("Enter city") },
        onValueChange = { newCity -> beerListViewModel.setCity(newCity) },
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
        ),
    )
}

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

@Composable
fun GetBeerList(viewModel: BeerListViewModel, context: Context = LocalContext.current){
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
    ){
        itemsIndexed(viewModel.filteredBeerList.value) { _, beer ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(text = beer.brand,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.width(200.dp))
                Column {
                    Text(text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(beer.drunkAt),
                        style = MaterialTheme.typography.bodySmall)
                    Text(text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(beer.drunkAt) + " h",
                        style = MaterialTheme.typography.bodySmall)
                    Text(text = beer.city,
                        style = MaterialTheme.typography.bodySmall)
                }
                IconButton(
                    onClick = {
                        viewModel.deleteBeer(beer, context)
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .background(DarkRed, RoundedCornerShape(4.dp))
                ) {
                    Icon(imageVector = Icons.Default.Delete,
                        tint = Alabaster,
                        contentDescription = "Delete")
                }
            }
            Divider()
        }
    }
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()