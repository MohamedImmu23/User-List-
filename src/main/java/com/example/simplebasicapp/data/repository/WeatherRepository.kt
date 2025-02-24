package com.example.simplebasicapp.data.repository

import android.util.Log
import com.example.simplebasicapp.data.local.WeatherDao
import com.example.simplebasicapp.data.model.WeatherEntity
import com.example.simplebasicapp.data.model.WeatherLocation
import com.example.simplebasicapp.data.model.WeatherResponse
import com.example.simplebasicapp.data.remote.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class WeatherRepository(private val api: WeatherService, private val dao: WeatherDao) {

    suspend fun fetchWeather(location: String, apiKey: String) {
        try {
            val response: WeatherResponse = api.getWeather(apiKey, location)
            Log.i("WeatherRepository", "API Response: $response")

            val rawJson = com.google.gson.Gson().toJson(response)
            Log.i("WeatherRepository", "Raw API JSON: $rawJson")
            Log.i("WeatherRepository", "Raw API JSON: ${com.google.gson.Gson().toJson(response)}")

            val currentWeather = response.current ?: throw NullPointerException("Current weather data is null")
            val weatherLocation = response.location ?: throw NullPointerException("Weather location data is null")

            val iconUrl = currentWeather.weatherIcons.firstOrNull() ?: ""

            // Convert API response to Room Entity
            val weatherEntity = WeatherEntity(
                temperature = currentWeather.temperature,
                humidity = currentWeather.humidity,
                iconUrl = iconUrl,
                location = WeatherLocation(weatherLocation.cityName)
            )

            // Save to Room database
            dao.insertWeather(weatherEntity)
            Log.i("WeatherRepository", "Inserting weather data: $weatherEntity")

        } catch (e: Exception) {
            println("Error fetching weather: ${e.message}")
        }
    }
    fun getStoredWeather(): Flow<WeatherEntity> = dao.getWeather()
        .distinctUntilChanged()
}