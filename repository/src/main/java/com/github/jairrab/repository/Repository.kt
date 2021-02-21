package com.github.jairrab.repository

import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.types.City
import com.github.jairrab.repository.implementation.responses.GetCitiesWeatherResponse
import com.github.jairrab.repository.implementation.responses.GetCityWeatherResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCitiesWeather(): Flow<List<WeatherCity>>

    suspend fun updateCitiesWeather(cities: List<City>): GetCitiesWeatherResponse

    fun getCityWeather(cityId: Int): Flow<WeatherCity>

    suspend fun updateCityWeather(cityId: Int): GetCityWeatherResponse

    suspend fun saveCityWeather(weatherCity: WeatherCity)
}