package com.github.jairrab.weatherapp.ui.city

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.jairrab.viewmodel.city.CityViewModel

@Composable
fun CityName(modifier: Modifier, viewModel: CityViewModel) {
    val text by viewModel.nameTextLd.observeAsState("")
    Text(text = text, modifier = modifier, textAlign = TextAlign.Center, fontSize = 24.sp)
}