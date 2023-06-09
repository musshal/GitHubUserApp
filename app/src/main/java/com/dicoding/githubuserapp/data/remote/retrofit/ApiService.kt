package com.dicoding.githubuserapp.data.remote.retrofit

import com.dicoding.githubuserapp.data.remote.response.UserResponse
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.data.remote.response.UsersResponse
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

    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String
    ): Call<ArrayList<UsersItem>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UsersItem>>
}