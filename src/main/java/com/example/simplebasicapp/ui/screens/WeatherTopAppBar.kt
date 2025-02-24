package com.example.simplebasicapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.simplebasicapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(weatherViewModel: WeatherViewModel) {
    val weather by weatherViewModel.weather.collectAsState()
    val weatherIcon by weatherViewModel.weatherIcon.collectAsState()

    LaunchedEffect(Unit) {
        Log.i("WeatherTopAppBar", "Fetching weather data from API...")
        weatherViewModel.fetchWeather(
            location = "india",
            apiKey = "eb8b83120a8c9c93e4f825463f9a401b"
        )

    }

    TopAppBar(
        title = { Text("Listing App", color = Color.White) },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7B61FF)),
        actions = {
            weather?.let {
                Text(
                    text = "${it.temperature.toInt()}° ${it.location.cityName}",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                weatherIcon?.let { iconUrl ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(iconUrl),
                            contentDescription = "Weather Icon",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                        )
                    }
                }

            }
        }
    )
}