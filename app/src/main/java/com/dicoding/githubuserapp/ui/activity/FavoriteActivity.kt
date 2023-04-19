package com.dicoding.githubuserapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Favorite User")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showNoData(true)
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