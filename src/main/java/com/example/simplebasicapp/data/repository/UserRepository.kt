package com.example.simplebasicapp.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.simplebasicapp.data.local.UserDao
import com.example.simplebasicapp.data.model.User
import com.example.simplebasicapp.data.paging.UserPagingSource
import com.example.simplebasicapp.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(val api: ApiService, private val dao: UserDao) {

    fun getPagedUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(
                    this,
                    searchQuery = ""
                )
            }
        ).flow
    }

    suspend fun fetchUsers(page: Int = 1, searchQuery: String = ""): List<User> {
        return try {

            val response = api.getUsers(page, count = 20)
            Log.d("UserRepository", "API Response: ${response.results.size} users received")

            if (response.results.isEmpty()) {
                Log.d("UserRepository", "No users found in the response.")
            }

            val users = response.results.map { apiUser ->
                User(
                    id = apiUser.email,
                    firstName = apiUser.name.first,
                    lastName = apiUser.name.last,
                    email = apiUser.email,
                    phone = apiUser.phone,
                    city = apiUser.location.city,
                    country = apiUser.location.country,
                    imageUrl = apiUser.picture.large
                )
            }

            withContext(Dispatchers.IO) {
                dao.clearUsers()
                dao.insertUsers(users)
                Log.d("UserRepository", "Users inserted into database: ${users.size}")
            }

            if (searchQuery.isNotEmpty()) {
                users.filter { user ->
                    user.firstName.contains(searchQuery, ignoreCase = true) ||
                            user.lastName.contains(searchQuery, ignoreCase = true)
                }
            } else {
                users
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching users", e)
            emptyList()
        }
    }

    suspend fun getUsersFromDb(): List<User> {
        val users = withContext(Dispatchers.IO) {
            dao.getAllUsers()
        }
        Log.d("UserRepository", "Retrieved ${users.size} users from DB")
        return users
    }

}