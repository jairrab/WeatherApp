package com.github.jairrab.weatherapp.ui.cities.adapter

import android.graphics.Color
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.weatherapp.R
import com.github.jairrab.weatherapp.databinding.CityHolderBinding

class CityHolder(
    private val binding: CityHolderBinding,
    private val onClick: (WeatherCity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var weatherCity: WeatherCity

    init {
        binding.root.setOnClickListener {
            onClick(weatherCity)
        }
    }

    fun update(item: WeatherCity) {
        weatherCity = item

        binding.name.text = item.cityName
        binding.weather.text = item.weatherCondition.joinToString { it.main }

        updateTemperature(item)
        updateFavorite(item)
        updateBackground(item)
    }

    private fun updateTemperature(item: WeatherCity) {
        val celsius = String.format("%.1f", item.temperature).toDouble()
        val temperature = "$celsius °C"
        binding.temperature.text = temperature
    }

    private fun updateFavorite(item: WeatherCity) {
        val resId = if (item.isFavorite) R.drawable.ic_baseline_favorite_24 else {
            R.drawable.ic_baseline_favorite_border_24
        }
        binding.favorite.setImageResource(resId)
        binding.favorite.isInvisible = !item.isFavorite
    }

    private fun updateBackground(item: WeatherCity) {
        val color = when {
            item.temperature < 0 -> "#1976D2"
            item.temperature >= 0 && item.temperature < 15 -> "#26C6DA"
            item.temperature >= 15 && item.temperature < 30 -> "#66BB6A"
            else -> "#FF7043"
        }.let { Color.parseColor(it) }

        binding.root.setCardBackgroundColor(color)
    }
}