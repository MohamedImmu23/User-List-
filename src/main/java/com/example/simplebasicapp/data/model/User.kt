package com.example.simplebasicapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data Model

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val city: String,
    val country: String,
    val imageUrl: String
)
