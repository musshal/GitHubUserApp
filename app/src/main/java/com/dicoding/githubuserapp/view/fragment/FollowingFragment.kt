package com.dicoding.githubuserapp.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.databinding.FragmentFollowingBinding
import com.dicoding.githubuserapp.model.UsersItem
import com.dicoding.githubuserapp.view.adapter.UserFollowingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var adapter: UserFollowingAdapter
    private lateinit var recyclerView: RecyclerView


    companion object {
        var EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        adapter = UserFollowingAdapter(arrayListOf())
        recyclerView = view.findViewById(R.id.rv_github_user_followings)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)



        val username = arguments?.getString(EXTRA_USERNAME).toString()

        getUserFollowings(username)

    }

    private fun getUserFollowings(username: String) {
        val client = ApiConfig.getApiService().getUserFollowings(username)

        client.enqueue(object : Callback<ArrayList<UsersItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersItem>>,
                response: Response<ArrayList<UsersItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUsersData(responseBody)
                    }
                } else {
                    Log.e("FollowingFragment", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {

            }
        })
    }

    private fun setUsersData(users: ArrayList<UsersItem>) {
        adapter.setData(users)
    }
}