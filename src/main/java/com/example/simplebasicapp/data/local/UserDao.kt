package com.example.simplebasicapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplebasicapp.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE firstName LIKE '%' || :query || '%' OR lastName LIKE '%' || :query || '%' ORDER BY firstName ASC")
    fun getPagedUsers(query: String): PagingSource<Int, User>

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Query("DELETE FROM users")
    fun clearUsers()

}