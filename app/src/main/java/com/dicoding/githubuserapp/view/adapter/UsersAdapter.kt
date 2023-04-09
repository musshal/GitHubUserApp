package com.dicoding.githubuserapp.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.model.UsersItem
import com.dicoding.githubuserapp.view.activity.DetailActivity
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(private val users: ArrayList<UsersItem>)
    : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_row_user,
        parent,
        false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(users[position].avatarUrl)
            .into(holder.civGithubUser)

        holder.tvGithubUsername.text = users[position].login
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val civGithubUser: CircleImageView = view.findViewById(R.id.civ_github_user)
        val tvGithubUsername: TextView = view.findViewById(R.id.tv_github_username)
        val tvGithubBio: TextView = view.findViewById(R.id.tv_github_bio)

        fun bind(user: UsersItem) {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_GITHUB_USER, user)
                intent.putExtra(DetailActivity.USERNAME, user.login)
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<UsersItem>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }
}