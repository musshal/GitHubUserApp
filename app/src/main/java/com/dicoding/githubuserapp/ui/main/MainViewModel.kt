package com.dicoding.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _users = MutableLiveData<ArrayList<UsersItem>?>()
    val users: MutableLiveData<ArrayList<UsersItem>?> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isFound = MutableLiveData<Boolean>()
    val isFound: LiveData<Boolean> = _isFound

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getUsers()
    }

    fun getUsers() {
        _isLoading.value = true
        _isError.value = false
        _isFound.value = false

        val client = ApiConfig.getApiService().getUsers()

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
                        _users.value = responseBody
                    }
                } else {
                    _isError.value = true
                    _users.value = ArrayList()

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

    fun findUsers(query: String) {
        _isLoading.value = true
        _isError.value = false
        _isFound.value = false

        val client = ApiConfig.getApiService().findUsers(query)

        client.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                _isLoading.value = false
                _isError.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (responseBody.items.isEmpty()) {
                            _isFound.value = true
                        }

                        _users.value = responseBody.items
                    }
                } else {
                    _isError.value = true

                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true

                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}