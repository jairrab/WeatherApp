package com.github.jairrab.repository.implementation

import com.github.jairrab.model.LocalDb
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.types.City
import com.github.jairrab.remote.WeatherMapApi
import com.github.jairrab.remote.models.CitiesApiResponse
import com.github.jairrab.remote.models.CityApiResponse
import com.github.jairrab.repository.mapper.WeatherMapper
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.Captor

class RepositoryImplTest : TestCase() {
    private lateinit var repository: RepositoryImpl

    private val localDb = mock<LocalDb>()
    private val weatherMapper = WeatherMapper()
    private val weatherMapApi = mock<WeatherMapApi>()

    private val cities = listOf(
        City.MANILA,
        City.PRAGUE,
        City.SEOUL,
    )

    public override fun setUp() {
        super.setUp()
        repository = RepositoryImpl(localDb, weatherMapper, weatherMapApi)
    }

    @Captor
    private var weatherCitiesCaptor = argumentCaptor<List<WeatherCity>>()

    fun testUpdateCitiesWeather() = runBlocking {
        val cityApiResponses = listOf(
            CityApiResponse(id = 1, name = "manila"),
            CityApiResponse(id = 2, name = "prague"),
            CityApiResponse(id = 3, name = "seoul"),
        )

        whenever(weatherMapApi.getCities("1701668,3067696,1835848"))
            .doReturn(CitiesApiResponse(3, cityApiResponses))

        val data = cityApiResponses.map { weatherMapper.map(it) }

        val cachedCities = listOf(
            WeatherCity(cityId = 1, cityName = "manila"),
            WeatherCity(cityId = 2, cityName = "prague", isFavorite = true),
            WeatherCity(cityId = 3, cityName = "seoul"),
        )

        data[1].isFavorite = true

        whenever(localDb.getCities()).thenReturn(flowOf(cachedCities))

        repository.updateCitiesWeather(cities)

        verify(localDb).saveCities(weatherCitiesCaptor.capture())

        assertEquals(data, weatherCitiesCaptor.firstValue)
    }

    @Captor
    private var cityIdsCaptor = argumentCaptor<String>()

    fun testApiFormattedCityIds() = runBlocking {
        repository.updateCitiesWeather(cities)

        verify(weatherMapApi).getCities(cityIdsCaptor.capture())

        assertEquals("1701668,3067696,1835848", cityIdsCaptor.firstValue)
    }
}