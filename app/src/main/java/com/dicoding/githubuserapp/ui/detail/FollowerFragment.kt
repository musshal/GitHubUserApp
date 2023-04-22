package com.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.ui.adapter.UsersAdapter

class FollowerFragment : Fragment() {

    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView
    private val followerViewModel by viewModels<FollowerViewModel>()

    companion object {
        const val USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)

        adapter = UsersAdapter(arrayListOf())

        recyclerView = view.findViewById(R.id.rv_github_user_followers)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val username = arguments?.getString(USERNAME).toString()

        iniObserver(view, username)
    }

    private fun iniObserver(view: View, username: String) {
        followerViewModel.getUserFollowers(username)

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, view)
        }

        followerViewModel.isError.observe(viewLifecycleOwner) {
            showError(it, view)
        }

        followerViewModel.followers.observe(viewLifecycleOwner) {
            if (it != null) {
                setUsersData(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean, view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean, view: View) {
        val errorMessage: TextView = view.findViewById(R.id.errorMessage)

        errorMessage.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun setUsersData(users: ArrayList<UsersItem>) {
        adapter.setData(users)
    }
}