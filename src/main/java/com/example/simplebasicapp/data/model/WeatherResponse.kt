package com.example.simplebasicapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: ApiLocation,
    @SerializedName("current") val current: Current
)

data class ApiLocation(
    @SerializedName("name") val cityName: String
)

data class Current(
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("weather_icons") val weatherIcons: List<String>
)