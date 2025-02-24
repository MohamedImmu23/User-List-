package com.example.simplebasicapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.simplebasicapp.data.model.User
import com.example.simplebasicapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.simplebasicapp.data.paging.UserPagingSource
import kotlinx.coroutines.flow.flatMapLatest

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val pagedUsers: Flow<PagingData<User>> = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPagingSource(repository, query) }
        ).flow.cachedIn(viewModelScope)
    }

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun fetchUsers(query: String = "") {
        viewModelScope.launch {
            val dbUsers = repository.getUsersFromDb()
            if (dbUsers.isEmpty()) {
                Log.d("UserViewModel", "No data in DB, fetching from API...")
                val apiUsers = repository.fetchUsers()
                _users.value = apiUsers
                Log.i("UserViewModel", "Fetched users from API: ${_users.value}")
            } else {
                _users.value = dbUsers
                Log.i("UserViewModel", "Fetched users from DB: ${_users.value}")
            }

            _users.value = if (query.isNotEmpty()) {
                _users.value.filter {
                    it.firstName.contains(query, ignoreCase = true) ||
                            it.lastName.contains(query, ignoreCase = true)
                }
            } else {
                _users.value

            }
            Log.d("UserViewModel", "ViewModel instance: ${this.hashCode()}")
        }
    }


    fun getUserById(userId: String): User? {
        return _users.value.find { it.id == userId }
    }
}