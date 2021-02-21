package com.github.jairrab.repository.mapper

import com.github.jairrab.model.models.Weather
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.remote.models.CityApiResponse
import java.util.*
import javax.inject.Inject
import com.github.jairrab.remote.models.city.Weather as WeatherRemote

class WeatherMapper @Inject constructor() {
    fun map(data: CityApiResponse): WeatherCity {
        return WeatherCity(
            cityId = data.id,
            cityName = data.name,
            temperature = data.main.temp,
            tempMax = data.main.temp_max,
            tempMin = data.main.temp_min,
            time = Date(data.dt * 1000L),
            weatherCondition = data.weather.map { map(it) }
        )
    }

    private fun map(data: WeatherRemote): Weather {
        return Weather(
            description = data.description,
            icon = data.icon,
            id = data.id,
            main = data.main
        )
    }
}