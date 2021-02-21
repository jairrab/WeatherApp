package com.github.jairrab.viewmodel.city.event

sealed class CityEvent {
    class ServerErrorEvent(val message: String?) : CityEvent()
    class ApiErrorEvent(val message: String?) : CityEvent()
}