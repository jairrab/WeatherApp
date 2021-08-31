package com.github.jairrab.weatherapp.ui.cities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.weatherapp.R

@Composable
fun WeatherRow(city: WeatherCity, action: (WeatherCity) -> Unit) = Column(
    modifier = Modifier
        .padding(16.dp)
        .clickable { action(city) }
        .fillMaxWidth()
        .shadow(6.dp)
        .background(Color(city.getBackgroundColor()), RoundedCornerShape(8.dp))
        .padding(8.dp)
) {
    Row {
        Text(
            text = city.temperature.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(0.dp)
                .weight(1f)
        )
        if (city.isFavorite) {
            Image(
                painterResource(R.drawable.ic_baseline_favorite_24),
                contentDescription = ""
            )
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = city.cityName,
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = city.weatherCondition.joinToString { it.main },
            fontSize = 20.sp,
        )
    }
}

private fun WeatherCity.getBackgroundColor() = when {
    temperature < 0 -> "#1976D2"
    temperature >= 0 && temperature < 15 -> "#26C6DA"
    temperature >= 15 && temperature < 30 -> "#66BB6A"
    else -> "#FF7043"
}.let { android.graphics.Color.parseColor(it) }