package com.dicoding.githubuserapp.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.githubuserapp.helper.FavoriteViewModelFactory
import com.dicoding.githubuserapp.ui.adapter.UsersAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteUsersAdapter
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initObserver()

        adapter = FavoriteUsersAdapter(arrayListOf())

        binding.rvUsers.apply {
            val manager = LinearLayoutManager(this@FavoriteActivity)

            adapter = this@FavoriteActivity.adapter
            layoutManager = manager
        }

    }

    private fun initObserver() {
        favoriteViewModel.getFavoriteUsers().observe(this) {
            val items = arrayListOf<UsersItem>()
            it.map {
                val item = UsersItem(login = it.login, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }

            binding.rvUsers.adapter = UsersAdapter(items)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.menu, menu)

        return true
    }

    private fun showNoData(isFound: Boolean) {
        binding.noData.visibility = if (isFound) View.VISIBLE else View.GONE
    }
}