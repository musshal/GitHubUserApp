package com.dicoding.githubuserapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
}