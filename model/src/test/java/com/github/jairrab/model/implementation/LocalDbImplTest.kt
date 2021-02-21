package com.github.jairrab.model.implementation

import androidx.room.Room
import com.github.jairrab.model.BaseTestClass
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.room.CityWeatherDb
import com.github.jairrab.model.room.RoomDb
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocalDbImplTest : BaseTestClass() {

    private lateinit var cityWeatherDb: CityWeatherDb
    private lateinit var db: RoomDb

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            context, RoomDb::class.java
        ).allowMainThreadQueries().build()
        cityWeatherDb = db.cityWeatherDb
    }

    @Test
    fun testSaveCity() = runBlocking {
        val city = WeatherCity(1, "manila", true)

        cityWeatherDb.saveCity(city)

        val testObserver = cityWeatherDb.getCity(1)

        assertEquals(city, testObserver.first())
    }

    @Test
    fun testSaveCities() = runBlocking {
        val cities = mutableListOf(
            WeatherCity(1, "manila", true),
            WeatherCity(2, "manila", true),
            WeatherCity(3, "manila", true),
        )

        cityWeatherDb.saveCities(cities)

        val testObserver = cityWeatherDb.getCities()

        assertEquals(cities, testObserver.first())

        val city = WeatherCity(4, "new york", true)

        cityWeatherDb.saveCity(city)

        cities.add(city)

        assertEquals(cities, testObserver.first())
    }

    override fun tearDown() {
        super.tearDown()
        db.close()
    }
}