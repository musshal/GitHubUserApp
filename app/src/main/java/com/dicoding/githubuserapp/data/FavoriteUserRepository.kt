package com.dicoding.githubuserapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDao
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository private constructor(
    private val application: Application,
    private val favoriteUserDao: FavoriteUserDao,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    init {
        val database = FavoriteUserDatabase.getInstance(application)
        val favoriteUserDao = database.favoriteUserDao()
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserDao.getFavoriteUsers()

    fun getFavoriteUser(id: Int): LiveData<List<FavoriteUser>> = favoriteUserDao.getFavoriteUser(id)

    fun insert(favoriteUser: FavoriteUser) = executorService.execute { favoriteUserDao.insert(favoriteUser) }

    fun update(favoriteUser: FavoriteUser) = executorService.execute { favoriteUserDao.update(favoriteUser) }

    fun delete(favoriteUser: FavoriteUser) = executorService.execute { favoriteUserDao.delete(favoriteUser) }
}