package com.dicoding.githubuserapp.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.model.UserResponse
import com.dicoding.githubuserapp.model.UsersItem
import com.dicoding.githubuserapp.view.activity.DetailActivity
import com.dicoding.githubuserapp.viewmodel.FollowerViewModel
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val username = users[position].login
        val client = ApiConfig.getApiService().getUser(username)

        client.enqueue(object : Callback<UserResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        holder.tvName.text = responseBody.name

                        holder.tvBio.text = "Bio: ${responseBody.bio}"
                        holder.tvLocation.text = "Loc: ${responseBody.location}"
                        holder.tvRepositories.text = "Repo: ${responseBody.publicRepos}"
                        holder.tvFollowers.text = "Followers: ${responseBody.followers}"
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {}
        })

        Glide.with(holder.itemView.context).load(users[position].avatarUrl).into(holder.civUser)

        holder.tvUsername.text = users[position].login

        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val civUser: CircleImageView = view.findViewById(R.id.civ_user)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvUsername: TextView = view.findViewById(R.id.tv_username)
        val tvBio: TextView = view.findViewById(R.id.tv_bio)
        val tvLocation: TextView = view.findViewById(R.id.tv_location)
        val tvRepositories: TextView = view.findViewById(R.id.tv_repositories)
        val tvFollowers: TextView = view.findViewById(R.id.tv_followers)

        fun bind(user: UsersItem) {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)

                intent.putExtra(DetailActivity.KEY_USER, user)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(usersData: ArrayList<UsersItem>) {
        users.clear()
        users.addAll(usersData)

        notifyDataSetChanged()
    }
}