package com.github.jairrab.model

import com.github.jairrab.model.models.WeatherCity
import kotlinx.coroutines.flow.Flow

interface LocalDb {
    fun getCities(): Flow<List<WeatherCity>>

    suspend fun saveCities(data: List<WeatherCity>)

    fun getCity(cityId: Int): Flow<WeatherCity>

    suspend fun saveCity(data: WeatherCity)
}