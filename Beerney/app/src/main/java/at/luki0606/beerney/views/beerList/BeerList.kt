@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package at.luki0606.beerney.views.beerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.EarthYellow
import at.luki0606.beerney.ui.theme.Ebony
import java.time.LocalDateTime
import java.time.ZoneId

@Preview(showBackground = true)
@Composable
fun Preview() {
    BeerList()
}

@Composable
fun BeerList(){
    var city by remember { mutableStateOf("") }
    var selectedBrand by remember { mutableStateOf("") }
    var selectedStartDate by remember { mutableLongStateOf(LocalDateTime.now().toMillis()) }
    var selectedEndDate by remember { mutableLongStateOf(LocalDateTime.now().toMillis()) }

   Column(
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally,
       modifier = Modifier
           .padding(16.dp)
   ) {
       Dropdown(selectedBrand) { newBrand -> selectedBrand = newBrand }
       Spacer(modifier = Modifier.height(4.dp))
       SelectCity(city) { newCity -> city = newCity }
       Spacer(modifier = Modifier.height(8.dp))
       GetStartAndEndDate(
           startDateMillis = selectedStartDate,
           endDateMillis = selectedEndDate,
           onStartDateChanged = { newStartDate -> selectedStartDate = newStartDate },
           onEndDateChanged = { newEndDate -> selectedEndDate = newEndDate }
       )
       Spacer(modifier = Modifier.height(4.dp))
       GetBeerList()
       Spacer(modifier = Modifier.height(4.dp))

   }
}

@Composable
fun Dropdown(selectedBrand: String, onBrandChanged: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val availableBrands = arrayOf("Gösser", "Zipfer", "Murauer", "Puntigamer")

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
            modifier = Modifier.menuAnchor()
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
                        onBrandChanged(brand)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectCity(city: String, onCityChanged: (String) -> Unit){
    OutlinedTextField(
        value = city,
        label = { Text("City") },
        leadingIcon = { Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = "Location") },
        placeholder = { Text("Enter city") },
        onValueChange = {newCity -> onCityChanged(newCity)
        },
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

@Composable
fun GetStartAndEndDate(
    startDateMillis: Long,
    onStartDateChanged: (Long) -> Unit,
    endDateMillis: Long,
    onEndDateChanged: (Long) -> Unit
) {
    val dateTime = LocalDateTime.now()

    val dateRangePickerState = remember {
        DateRangePickerState(
            initialSelectedStartDateMillis = startDateMillis,
            initialDisplayedMonthMillis = null,
            initialSelectedEndDateMillis = endDateMillis,
            initialDisplayMode = DisplayMode.Input,
            yearRange = (dateTime.year - 10)..(dateTime.year + 10)
        )
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .background(Ebony),
        )
        val selectedStartDate = dateRangePickerState.selectedStartDateMillis
        val selectedEndDate = dateRangePickerState.selectedEndDateMillis

        DisposableEffect(selectedStartDate, selectedEndDate) {
            if (selectedStartDate != null) {
                onStartDateChanged(selectedStartDate)
            }
            if (selectedEndDate != null) {
                onEndDateChanged(selectedEndDate)
            }
            onDispose { }
        }

    }
}

@Composable
fun GetBeerList(){
    LazyColumn(
        Modifier.height(300.dp)
            .fillMaxWidth(),
    ){
        items(10) {
            Row {
                Text(text = "Beer", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Date", style = MaterialTheme.typography.bodySmall)
                    Text(text = "time", style = MaterialTheme.typography.bodySmall)
                    Text(text = "City", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { /* TODO: Delete beer */},
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Red)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Divider()
        }
    }
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()