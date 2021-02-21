package com.github.jairrab.repository.implementation.responses

import com.github.jairrab.model.models.WeatherCity

sealed class GetCitiesWeatherResponse {
    class Success(val data: List<WeatherCity>) : GetCitiesWeatherResponse()
    class ServerError(val error: Throwable) : GetCitiesWeatherResponse()
    class ApiError(val error: Throwable) : GetCitiesWeatherResponse()
}