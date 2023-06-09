package com.dicoding.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _followings = MutableLiveData<ArrayList<UsersItem>?>()
    val followings: MutableLiveData<ArrayList<UsersItem>?> = _followings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    fun getUserFollowings(username: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getUserFollowings(username)

        client.enqueue(object : Callback<ArrayList<UsersItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersItem>>,
                response: Response<ArrayList<UsersItem>>
            ) {
                _isLoading.value = false
                _isError.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _followings.value = responseBody
                    }
                } else {
                    _isError.value = true

                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true

                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}