package com.dicoding.githubuserapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.databinding.ItemRowUserBinding

class FavoriteUsersAdapter(private val listener: OnFavoriteUserClick? = null) :
    RecyclerView.Adapter<FavoriteUsersAdapter.ViewHolder>() {

    private val favoriteUsers = ArrayList<FavoriteUser>()

    interface OnFavoriteUserClick {
        fun onFavoriteUserClick(login: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                tvUsername.text = favoriteUser.login

                Glide.with(itemView)
                    .load(favoriteUser.avatarUrl)
                    .into(civUser)

                root.setOnClickListener {
                    listener?.onFavoriteUserClick(favoriteUser.login)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteUsersAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_user,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteUsers[position])
    }

    override fun getItemCount(): Int = favoriteUsers.size
}