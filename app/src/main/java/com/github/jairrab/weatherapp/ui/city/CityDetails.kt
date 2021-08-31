package com.github.jairrab.weatherapp.ui.city

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jairrab.viewmodel.city.CityViewModel

@Composable
fun CityDetails(viewModel: CityViewModel) {
    Column(modifier = Modifier.padding(top = 100.dp, start = 24.dp, end = 24.dp)) {
        Row {
            CityName(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f),
                viewModel = viewModel
            )
            Favorite(viewModel)
        }
        Row(modifier = Modifier.padding(top = 24.dp)) {
            Temperature(
                modifier = Modifier
                    .width(0.dp)
                    .weight(0.8f)
                    .padding(end = 24.dp),
                viewModel = viewModel
            )
            Column(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f),
            ) {
                Weather(viewModel)
                HighLowTemp(viewModel)
            }
        }
    }
}