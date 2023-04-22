package com.dicoding.githubuserapp.repository

import android.app.Application
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDao
import com.dicoding.githubuserapp.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)

        favoriteUserDao = db.favoriteUserDao()
    }

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }

    fun isFavorited(login: String) = favoriteUserDao.isFavorited(login)

    fun getFavoriteUsers() = favoriteUserDao.getFavoriteUsers()
}