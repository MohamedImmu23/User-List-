package com.example.simplebasicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplebasicapp.data.model.WeatherEntity
import com.example.simplebasicapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    val weather: StateFlow<WeatherEntity?> = repository.getStoredWeather()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val weatherIcon: StateFlow<String?> = repository.getStoredWeather()
        .map { it?.iconUrl }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun fetchWeather(location: String, apiKey: String) {
        viewModelScope.launch {
            repository.fetchWeather(location, apiKey)
        }
    }
}