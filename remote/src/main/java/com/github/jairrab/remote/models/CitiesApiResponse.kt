package com.github.jairrab.remote.models

data class CitiesApiResponse(
    val cnt: Int = 0,
    val list: List<CityApiResponse> = emptyList()
)