package com.github.jairrab.repository.implementation.responses

import com.github.jairrab.model.models.WeatherCity

sealed class GetCityWeatherResponse {
    class Success(val data: WeatherCity) : GetCityWeatherResponse()
    class ServerError(val error: Throwable) : GetCityWeatherResponse()
    class ApiError(val error: Throwable) : GetCityWeatherResponse()
}