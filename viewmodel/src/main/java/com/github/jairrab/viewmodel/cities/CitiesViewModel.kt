package com.github.jairrab.viewmodel.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewModelScope
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.model.types.City
import com.github.jairrab.repository.Repository
import com.github.jairrab.repository.implementation.responses.GetCitiesWeatherResponse
import com.github.jairrab.viewmodel.base.BaseViewModel
import com.github.jairrab.viewmodel.cities.event.CitiesEvent
import com.github.jairrab.viewmodel.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {
    private val _bottomSpinnerVisibilityLd = MutableLiveData(false)
    val bottomSpinnerVisibilityLd: LiveData<Boolean> get() = _bottomSpinnerVisibilityLd

    private val _swipeRefreshVisibilityLd = MutableLiveData(false)
    val swipeRefreshVisibilityLd: LiveData<Boolean> get() = _swipeRefreshVisibilityLd

    private val _citiesLd = MutableLiveData<List<WeatherCity>>()
    val citiesLd = map(_citiesLd) { it }

    private val _eventLd = MutableLiveData<Event<CitiesEvent>>()
    val eventLd: LiveData<Event<CitiesEvent>> get() = _eventLd

    init {
        viewModelScope.launch {
            repository.getCitiesWeather().collect {
                _citiesLd.value = it
            }
        }

        viewModelScope.launch {
            _bottomSpinnerVisibilityLd.value = true
            updateCities()
            _bottomSpinnerVisibilityLd.value = false
        }
    }

    fun pullToRefresh() = viewModelScope.launch {
        updateCities()
        delay(250)
        _swipeRefreshVisibilityLd.value = false
    }

    private suspend fun updateCities() {
        val cities = listOf(City.MANILA, City.PRAGUE, City.SEOUL)

        when (val response = repository.updateCitiesWeather(cities)) {
            is GetCitiesWeatherResponse.ApiError -> {
                _eventLd.value = Event(CitiesEvent.ApiErrorEvent(response.error.message))
            }
            is GetCitiesWeatherResponse.ServerError -> {
                _eventLd.value = Event(CitiesEvent.ServerErrorEvent(response.error.message))
            }
            is GetCitiesWeatherResponse.Success -> {
                Timber.v("Received ${response.data.size} items")
            }
        }
    }
}