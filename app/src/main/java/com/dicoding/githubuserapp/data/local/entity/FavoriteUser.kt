package com.dicoding.githubuserapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    var login: String = "",
    var avatarUrl: String? = null,
)
