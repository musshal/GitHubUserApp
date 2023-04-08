package com.dicoding.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.model.UsersItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    private val _followers = MutableLiveData<ArrayList<UsersItem>>()
    val followers: LiveData<ArrayList<UsersItem>> = _followers

    companion object {
        private const val TAG = "FollowerViewModel"
    }

    fun getUserFollowers(username: String) {
        val client = ApiConfig.getApiService().getUserFollowers(username)

        client.enqueue(object : Callback<ArrayList<UsersItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersItem>>,
                response: Response<ArrayList<UsersItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _followers.value = responseBody
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}