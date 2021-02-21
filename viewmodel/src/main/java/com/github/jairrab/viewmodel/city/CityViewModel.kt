package com.github.jairrab.viewmodel.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewModelScope
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.repository.Repository
import com.github.jairrab.repository.implementation.responses.GetCityWeatherResponse
import com.github.jairrab.viewmodel.base.BaseViewModel
import com.github.jairrab.viewmodel.city.event.CityEvent
import com.github.jairrab.viewmodel.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository,
) : BaseViewModel() {
    private val argsLd = savedStateHandle.getLiveData<Int>(CITY_ID)
    private val cityId get() = argsLd.value!!

    private val cityLd = MutableLiveData<WeatherCity>()
    private val city get() = cityLd.value!!

    val nameTextLd = map(cityLd) { it.cityName }

    val temperatureTextLd = map(cityLd) {
        val temperature = String.format("%.1f", it.temperature).toDouble()
        "$temperature°C"
    }

    val tempMinMaxTextLd = map(cityLd) {
        val tempMax = it.tempMax.roundToInt()
        val tempMin = it.tempMin.roundToInt()
        "High $tempMax°C / Low $tempMin°C"
    }

    val weatherTextLd = map(cityLd) {
        it.weatherCondition.joinToString { it.main }
    }

    val isFavoriteLd = map(cityLd) { it.isFavorite }

    private val _eventLd = MutableLiveData<Event<CityEvent>>()
    val eventLd: LiveData<Event<CityEvent>> get() = _eventLd

    init {
        viewModelScope.launch {
            repository.getCityWeather(cityId).collect {
                cityLd.value = it
            }
        }

        viewModelScope.launch { updateCity() }
    }

    private suspend fun updateCity() {
        when (val response = repository.updateCityWeather(cityId)) {
            is GetCityWeatherResponse.ApiError -> {
                _eventLd.value = Event(CityEvent.ApiErrorEvent(response.error.message))
            }
            is GetCityWeatherResponse.ServerError -> {
                _eventLd.value = Event(CityEvent.ServerErrorEvent(response.error.message))
            }
            is GetCityWeatherResponse.Success -> {
                Timber.v("GetCityWeatherResponse.Success")
            }
        }
    }

    fun toggleFavorite() = viewModelScope.launch {
        city.isFavorite = !city.isFavorite
        cityLd.value = city
        repository.saveCityWeather(city)
    }

    companion object {
        const val CITY_ID = "CITY_ID"
    }
}