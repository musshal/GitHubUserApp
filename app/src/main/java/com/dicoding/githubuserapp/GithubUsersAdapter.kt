package com.dicoding.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class GithubUsersAdapter(private val listGithubUsers: ArrayList<GithubUsersResponseItem>)
    : RecyclerView.Adapter<GithubUsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_row_github_user,
        parent,
        false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listGithubUsers[position].avatarUrl)
            .into(holder.civGithubUser)

        holder.tvGithubUsername.text = listGithubUsers[position].login
    }

    override fun getItemCount(): Int = listGithubUsers.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val civGithubUser: CircleImageView = view.findViewById(R.id.civ_github_user)
        val tvGithubUsername: TextView = view.findViewById(R.id.tv_github_username)
        val tvGithubLocation: TextView = view.findViewById(R.id.tv_github_location)
    }

    fun setData(data: ArrayList<GithubUsersResponseItem>) {
        listGithubUsers.clear()
        listGithubUsers.addAll(data)
        notifyDataSetChanged()
    }
}