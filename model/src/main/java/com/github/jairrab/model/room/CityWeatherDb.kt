package com.github.jairrab.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.jairrab.model.models.WeatherCity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface CityWeatherDb {
    @Query("DELETE FROM CITY_TABLE")
    suspend fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(item: WeatherCity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCities(item: List<WeatherCity>)

    @Query(
        """
            SELECT * FROM CITY_TABLE
            ORDER BY cityTableCityName 
        """
    )
    fun getCities(): Flow<List<WeatherCity>>

    @ExperimentalCoroutinesApi
    fun getCitiesDistinctUntilChanged(): Flow<List<WeatherCity>> {
        return getCities().distinctUntilChanged()
    }

    @Query(
        """
            SELECT * FROM CITY_TABLE
            WHERE cityTableCityId = :id
        """
    )
    fun getCity(id: Int): Flow<WeatherCity>

    @ExperimentalCoroutinesApi
    fun getCityDistinctUntilChanged(id: Int): Flow<WeatherCity> {
        return getCity(id).distinctUntilChanged()
    }
}
