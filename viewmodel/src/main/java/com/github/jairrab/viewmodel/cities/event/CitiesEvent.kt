package com.github.jairrab.viewmodel.cities.event

sealed class CitiesEvent {
    class ServerErrorEvent(val message: String?) : CitiesEvent()
    class ApiErrorEvent(val message: String?) : CitiesEvent()
}