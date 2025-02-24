package com.example.simplebasicapp.data.remote

import com.example.simplebasicapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("current")
    suspend fun getWeather(
        @Query("access_key") apiKey: String,
        @Query("query") location: String
    ): WeatherResponse
}
