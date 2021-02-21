package com.github.jairrab.model.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CITY_TABLE")
data class WeatherCity(
    @PrimaryKey @ColumnInfo(name = "cityTableCityId") val cityId: Int = 0,
    @ColumnInfo(name = "cityTableCityName") val cityName: String = "",
    @ColumnInfo(name = "cityTableFavorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "cityTableTemperature") val temperature: Double = 0.0,
    @ColumnInfo(name = "cityTableTempMax") val tempMax: Double = 0.0,
    @ColumnInfo(name = "cityTableTempMin") val tempMin: Double = 0.0,
    @ColumnInfo(name = "cityTableTime") val time: Date = Date(),
    @ColumnInfo(name = "cityTableWeatherCondition") val weatherCondition: List<Weather> = emptyList(),
)