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

class FollowingFragment : Fragment() {

    private lateinit var adapter: DetailUsersAdapter
    private lateinit var recyclerView: RecyclerView
    private val followingViewModel by viewModels<FollowingViewModel>()

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

        val username = arguments?.getString(USERNAME).toString()

        initRecyclerView(view)
        initObserver(view, username)
    }

    private fun initRecyclerView(view: View) {
        adapter = DetailUsersAdapter(arrayListOf())
        recyclerView = view.findViewById(R.id.rv_github_user_followings)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    private fun initObserver(view: View, username: String) {
        followingViewModel.getUserFollowings(username)

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, view)
        }

        followingViewModel.isError.observe(viewLifecycleOwner) {
            showError(it, view)
        }

        followingViewModel.followings.observe(viewLifecycleOwner) {
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