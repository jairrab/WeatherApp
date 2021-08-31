package com.github.jairrab.weatherapp.ui.city

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp
import com.github.jairrab.viewmodel.city.CityViewModel

@Composable
fun HighLowTemp(viewModel: CityViewModel) {
    val text by viewModel.tempMinMaxTextLd.observeAsState("")
    Text(text = text, fontSize = 18.sp)
}