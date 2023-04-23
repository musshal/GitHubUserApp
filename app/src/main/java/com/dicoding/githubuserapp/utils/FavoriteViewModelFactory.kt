package com.dicoding.githubuserapp.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserapp.ui.detail.DetailViewModel
import com.dicoding.githubuserapp.ui.favorite.FavoriteViewModel

class FavoriteViewModelFactory private constructor(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteViewModelFactory::class.java) {
                    INSTANCE = FavoriteViewModelFactory(application)
                }
            }

            return INSTANCE as FavoriteViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}