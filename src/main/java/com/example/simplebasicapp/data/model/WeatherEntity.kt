package com.example.simplebasicapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

// Weather Data Model
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val temperature: Double,
    val humidity: Int,
    val iconUrl: String?,

    @Embedded val location: WeatherLocation

)

data class WeatherLocation(
    val cityName: String
)
