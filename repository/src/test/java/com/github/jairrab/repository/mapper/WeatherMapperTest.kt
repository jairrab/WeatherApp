package com.github.jairrab.repository.mapper

import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.remote.models.CityApiResponse
import com.github.jairrab.remote.models.city.Main
import com.github.jairrab.remote.models.city.Weather
import junit.framework.TestCase
import org.junit.Test
import java.util.*
import com.github.jairrab.model.models.Weather as Weather1

class WeatherMapperTest : TestCase() {
    private lateinit var mapper: WeatherMapper

    public override fun setUp() {
        super.setUp()
        mapper = WeatherMapper()
    }

    @Test
    fun testMap() {
        val input = CityApiResponse(
            base = "a",
            dt = 1000,
            id = 1,
            main = Main(temp = 2.0, temp_min = 1.0, temp_max = 3.0),
            name = "name",
            weather = listOf(
                Weather(main = "weather1"),
                Weather(main = "weather2")
            )
        )

        val expected = WeatherCity(
            cityId = 1,
            cityName = "name",
            isFavorite = false,
            temperature = 2.0,
            tempMax = 3.0,
            tempMin = 1.0,
            time = Date(1000000L),
            weatherCondition = listOf(
                Weather1(main = "weather1"),
                Weather1(main = "weather2"),
            )
        )

        assertEquals(expected, mapper.map(input))
    }
}