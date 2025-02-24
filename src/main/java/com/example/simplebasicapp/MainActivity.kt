package com.example.simplebasicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.simplebasicapp.data.local.AppDatabase
import com.example.simplebasicapp.data.remote.ApiService
import com.example.simplebasicapp.data.remote.WeatherService
import com.example.simplebasicapp.data.repository.UserRepository
import com.example.simplebasicapp.data.repository.WeatherRepository
import com.example.simplebasicapp.ui.AppNavigation
import com.example.simplebasicapp.viewmodel.UserViewModel
import com.example.simplebasicapp.viewmodel.WeatherViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {

        val weatherRetrofit = Retrofit.Builder().baseUrl("https://api.weatherstack.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val weatherService = weatherRetrofit.create(WeatherService::class.java)

        val retrofit = Retrofit.Builder().baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val apiService = retrofit.create(ApiService::class.java)

        // Initialize Room Database
        val db by lazy {
            Room.databaseBuilder(
                applicationContext, AppDatabase::class.java, "user-database"
            ).build()
        }

        val userDao = db.userDao()
        val weatherDao = db.weatherDao()
        val weatherRepository = WeatherRepository(weatherService, weatherDao)

        // Initialize UserRepository with Retrofit and Room DAO
        val userRepository = UserRepository(apiService, userDao)

        // Create ViewModel with the UserRepository
        val viewModel = UserViewModel(userRepository)
        val weatherViewModel = WeatherViewModel(weatherRepository)


        val navController = rememberNavController()

        AppNavigation(navController, weatherViewModel, viewModel)
    }

}