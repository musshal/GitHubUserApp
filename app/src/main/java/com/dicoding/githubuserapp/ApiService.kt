package com.dicoding.githubuserapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getGithubUsers(): Call<ArrayList<GithubUsersResponseItem>>

    @GET("search/users")
    fun findGithubUsers(
        @Query("q") q: String
    ): Call<GithubUsersResponse>
}