package com.github.jairrab.remote

import com.github.jairrab.remote.models.CitiesApiResponse
import com.github.jairrab.remote.models.CityApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherMapApi {
    @GET("data/2.5/weather?units=metric")
    suspend fun getCity(
        @Query("id") id: Int,
    ): CityApiResponse

    @GET("data/2.5/group?units=metric")
    suspend fun getCities(
        @Query("id") ids: String,
    ): CitiesApiResponse
}