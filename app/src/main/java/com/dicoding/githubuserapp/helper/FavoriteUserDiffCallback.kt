package com.dicoding.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser

class FavoriteUserDiffCallback(private val oldFavoriteUsers: List<FavoriteUser>, private val newFavoriteUsers: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteUsers.size

    override fun getNewListSize(): Int = newFavoriteUsers.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUsers[oldItemPosition].login == newFavoriteUsers[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldFavoriteUsers[oldItemPosition]
        val newItem = newFavoriteUsers[newItemPosition]

        return oldItem.login == newItem.login && oldItem.avatarUrl == newItem.avatarUrl
    }
}