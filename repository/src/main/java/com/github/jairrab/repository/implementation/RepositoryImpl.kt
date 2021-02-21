package com.github.jairrab.repository.implementation

import com.github.jairrab.model.LocalDb
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.types.City
import com.github.jairrab.remote.WeatherMapApi
import com.github.jairrab.repository.Repository
import com.github.jairrab.repository.implementation.responses.GetCitiesWeatherResponse
import com.github.jairrab.repository.implementation.responses.GetCityWeatherResponse
import com.github.jairrab.repository.mapper.WeatherMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val localDb: LocalDb,
    private val weatherMapper: WeatherMapper,
    private val weatherMapApi: WeatherMapApi,
) : Repository {
    override fun getCitiesWeather(): Flow<List<WeatherCity>> {
        return localDb.getCities()
    }

    override suspend fun updateCitiesWeather(cities: List<City>): GetCitiesWeatherResponse {
        return try {
            val ids = getApiFormattedCityIds(cities)
            val response = weatherMapApi.getCities(ids)

            //transforms API responses, then checks cache to update favorite setting of each city
            val data = response.list.map { weatherMapper.map(it) }.apply {
                val cachedCities = localDb.getCities().first()
                forEach { city ->
                    city.isFavorite = cachedCities.firstOrNull { city.cityId == it.cityId }
                        ?.isFavorite ?: false
                }
            }

            localDb.saveCities(data)
            GetCitiesWeatherResponse.Success(data)
        } catch (e: UnknownHostException) {
            GetCitiesWeatherResponse.ServerError(e)
        } catch (e: Exception) {
            GetCitiesWeatherResponse.ApiError(e)
        }
    }

    override fun getCityWeather(cityId: Int): Flow<WeatherCity> {
        return localDb.getCity(cityId)
    }

    override suspend fun updateCityWeather(cityId: Int): GetCityWeatherResponse {
        return try {
            val response = weatherMapApi.getCity(cityId)

            //transforms API responses, then checks cache to update favorite setting of each city
            val data = weatherMapper.map(response).apply {
                val cachedCity = localDb.getCity(cityId).first()
                isFavorite = cachedCity.isFavorite
            }

            localDb.saveCity(data)
            GetCityWeatherResponse.Success(data)
        } catch (e: UnknownHostException) {
            GetCityWeatherResponse.ServerError(e)
        } catch (e: Exception) {
            GetCityWeatherResponse.ApiError(e)
        }
    }

    override suspend fun saveCityWeather(weatherCity: WeatherCity) {
        localDb.saveCity(weatherCity)
    }

    /** Takes [cities] and returns a list of their ids in this format: "1234,5678,9101" */
    private fun getApiFormattedCityIds(cities: List<City>) =
        cities.map { it.id }.toString()
            .substringAfter('[')
            .substringBeforeLast(']')
            .replace(" ", "")
}