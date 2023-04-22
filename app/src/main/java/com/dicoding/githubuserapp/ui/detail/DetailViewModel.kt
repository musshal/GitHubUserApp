package com.dicoding.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig
import com.dicoding.githubuserapp.data.remote.response.UserResponse
import com.dicoding.githubuserapp.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _user = MutableLiveData<UserResponse?>()
    val user: MutableLiveData<UserResponse?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getUser(username: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getUser(username)

        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                _isError.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                } else {
                    _isError.value = true

                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true

                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun insert(favoriteUser: FavoriteUser) {
        favoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        favoriteUserRepository.delete(favoriteUser)
    }

    fun isFavorited(login: String): LiveData<Boolean> = favoriteUserRepository.isFavorited(login)
}