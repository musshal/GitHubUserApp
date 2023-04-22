package com.dicoding.githubuserapp.data.remote.retrofit

import com.dicoding.githubuserapp.data.remote.response.UserResponse
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("users")
    fun getUsers(): Call<ArrayList<UsersItem>>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @Headers("Authorization: token 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")
    @GET("search/users")
    fun findUsers(
        @Query("q") q: String
    ): Call<UsersResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String
    ): Call<ArrayList<UsersItem>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UsersItem>>
}