package com.dicoding.githubuserapp.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("bio")
    val bio: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int
)