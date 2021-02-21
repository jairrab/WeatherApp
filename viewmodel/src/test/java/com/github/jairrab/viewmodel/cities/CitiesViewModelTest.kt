package com.github.jairrab.viewmodel.cities

import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.repository.Repository
import com.github.jairrab.repository.implementation.responses.GetCitiesWeatherResponse
import com.github.jairrab.viewmodel.BaseTestClass
import com.github.jairrab.viewmodel.cities.event.CitiesEvent
import com.github.jairrab.viewmodel.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class CitiesViewModelTest : BaseTestClass() {
    private lateinit var viewModel: CitiesViewModel

    private val repository = mock<Repository>()

    private val cities = listOf(
        WeatherCity(1),
        WeatherCity(2),
        WeatherCity(3),
    )

    @Test
    fun testInitApiSuccess() = runBlocking {
        whenever(repository.updateCitiesWeather(any()))
            .doReturn(GetCitiesWeatherResponse.Success(cities))

        whenever(repository.getCitiesWeather())
            .doReturn(flowOf(cities))

        viewModel = CitiesViewModel(repository)

        assertEquals(cities, viewModel.citiesLd.getOrAwaitValue())
    }

    @Test
    fun testInitApiError() = runBlocking {
        whenever(repository.updateCitiesWeather(any()))
            .doReturn(GetCitiesWeatherResponse.ApiError(Error("error message")))

        viewModel = CitiesViewModel(repository)

        val citiesEvent = viewModel.eventLd.getOrAwaitValue().getContentIfNotHandled()

        assertThat(
            "ApiErrorEvent is expected",
            citiesEvent is CitiesEvent.ApiErrorEvent
        )

        assertEquals("error message", (citiesEvent as? CitiesEvent.ApiErrorEvent)?.message)
    }

    @Test
    fun testInitServerError() = runBlocking {
        whenever(repository.updateCitiesWeather(any()))
            .doReturn(GetCitiesWeatherResponse.ServerError(Error("error message")))

        viewModel = CitiesViewModel(repository)

        val citiesEvent = viewModel.eventLd.getOrAwaitValue().getContentIfNotHandled()

        assertThat(
            "ApiErrorEvent is expected",
            citiesEvent is CitiesEvent.ServerErrorEvent
        )

        assertEquals("error message", (citiesEvent as? CitiesEvent.ServerErrorEvent)?.message)
    }
}