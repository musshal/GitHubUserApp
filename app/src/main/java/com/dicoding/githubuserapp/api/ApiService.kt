package com.dicoding.githubuserapp.api

import com.dicoding.githubuserapp.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(): Call<ArrayList<UsersItem>>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("search/users")
    fun findUsers(
        @Query("q") q: String
    ): Call<UsersResponse>
}