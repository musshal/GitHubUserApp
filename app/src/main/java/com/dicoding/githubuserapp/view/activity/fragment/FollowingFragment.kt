package com.dicoding.githubuserapp.view.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.model.UsersItem
import com.dicoding.githubuserapp.view.adapter.UsersAdapter
import com.dicoding.githubuserapp.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val USERNAME = "username"
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

        adapter = UsersAdapter(arrayListOf())
        recyclerView = view.findViewById(R.id.rv_github_user_followings)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val username = arguments?.getString(USERNAME).toString()
        val followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]

        followingViewModel.getUserFollowings(username)

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, view)
        }

        followingViewModel.isError.observe(viewLifecycleOwner) {
            showError(it, view)
        }

        followingViewModel.followings.observe(viewLifecycleOwner) {followings ->
            setUsersData(followings)
        }
    }

    private fun setUsersData(users: ArrayList<UsersItem>) {
        adapter.setData(users)
    }

    private fun showLoading(isLoading: Boolean, view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean, view: View) {
        val errorMessage: TextView = view.findViewById(R.id.errorMessage)

        errorMessage.visibility = if (isError) View.VISIBLE else View.GONE
    }
}