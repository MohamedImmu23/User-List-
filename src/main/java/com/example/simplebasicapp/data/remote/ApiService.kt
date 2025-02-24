package com.example.simplebasicapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") count: Int = DEFAULT_PAGE_SIZE
    ): RandomUserResponse

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}

data class RandomUserResponse(
    val results: List<ApiUser>
)

data class ApiUser(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val phone: String,
    val cell: String,
    val picture: Picture
)

data class Name(val title: String, val first: String, val last: String)
data class Location(val city: String, val state: String, val country: String)
data class Login(val uuid: String)
data class Dob(val date: String, val age: Int)
data class Picture(val large: String, val medium: String, val thumbnail: String)