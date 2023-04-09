package com.dicoding.githubuserapp.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.model.UsersItem
import de.hdodenhof.circleimageview.CircleImageView

class UserFollowingAdapter(private val users: ArrayList<UsersItem>) :
    RecyclerView.Adapter<UserFollowingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_user_following,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGithubUsername.text = users[position].login
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val civGithubUser: CircleImageView = view.findViewById(R.id.civ_github_user_following)
        val tvGithubUsername: TextView = view.findViewById(R.id.tv_github_username_following)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<UsersItem>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }
}