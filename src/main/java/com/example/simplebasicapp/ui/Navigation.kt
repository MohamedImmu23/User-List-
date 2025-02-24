package com.example.simplebasicapp.ui

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.simplebasicapp.data.model.User
import com.example.simplebasicapp.ui.screens.SplashScreen
import com.example.simplebasicapp.ui.screens.UserDetailScreen
import com.example.simplebasicapp.ui.screens.UserListScreen
import com.example.simplebasicapp.ui.screens.WeatherTopAppBar
import com.example.simplebasicapp.viewmodel.UserViewModel
import com.example.simplebasicapp.viewmodel.WeatherViewModel
import com.google.gson.Gson

@Composable
fun AppNavigation(
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    viewModel: UserViewModel
) {
    NavHost(navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("list") {
            Scaffold(
                topBar = { WeatherTopAppBar(weatherViewModel = weatherViewModel) }
            ) { paddingValues ->
                UserListScreen(
                    viewModel = viewModel,
                    weatherViewModel = weatherViewModel,
                    navController = navController,
                    paddingValues = paddingValues
                )
            }
        }
        composable("details/{user}") { backStackEntry ->

            val userJson = backStackEntry.arguments?.getString("user")
            val user = userJson?.let { Gson().fromJson(it, User::class.java) }

            Log.i("Navigation", "User found: $user")

            if (user != null) {
                UserDetailScreen(user = user, navController = navController)
            } else {
                Text("User not found!")
            }
        }

    }
}
