package com.github.jairrab.model.implementation

import com.github.jairrab.model.LocalDb
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.room.RoomDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDbImpl @Inject constructor(
    private val roomDb: RoomDb
) : LocalDb {
    @ExperimentalCoroutinesApi
    override fun getCities(): Flow<List<WeatherCity>> {
        return roomDb.cityWeatherDb.getCitiesDistinctUntilChanged()
    }

    override suspend fun saveCities(data: List<WeatherCity>) {
        roomDb.cityWeatherDb.saveCities(data)
    }

    @ExperimentalCoroutinesApi
    override fun getCity(cityId: Int): Flow<WeatherCity> {
        return roomDb.cityWeatherDb.getCityDistinctUntilChanged(cityId)
    }

    override suspend fun saveCity(data: WeatherCity) {
        roomDb.cityWeatherDb.saveCity(data)
    }
}