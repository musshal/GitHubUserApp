package com.dicoding.githubuserapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UsersResponse(
	@field:SerializedName("items")
	val items: ArrayList<UsersItem>
)

@Parcelize
data class UsersItem(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,
) : Parcelable