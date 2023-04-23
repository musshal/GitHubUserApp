package com.dicoding.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.githubuserapp.helper.FavoriteViewModelFactory
import com.dicoding.githubuserapp.ui.adapter.UsersAdapter
import com.dicoding.githubuserapp.ui.setting.SettingActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun initObserver() {
        favoriteViewModel.getFavoriteUsers().observe(this) { favoriteUser ->
            val favoriteUsers = arrayListOf<UsersItem>()

            favoriteUser.map {
                val user = UsersItem(login = it.login, avatarUrl = it.avatarUrl.toString())
                favoriteUsers.add(user)
            }

            if (favoriteUser.isEmpty()) {
                setFavoriteUsers(favoriteUsers)

                binding.noData.visibility = View.VISIBLE
            } else {
                setFavoriteUsers(favoriteUsers)
            }
        }
    }

    private fun setFavoriteUsers(favoriteUsers: ArrayList<UsersItem>) {
        binding.rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.rvUsers.adapter = UsersAdapter(favoriteUsers)
    }
}