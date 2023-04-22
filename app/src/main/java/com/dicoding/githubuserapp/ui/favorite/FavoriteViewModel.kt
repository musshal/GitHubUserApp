package com.dicoding.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserRepository.getFavoriteUsers()
}