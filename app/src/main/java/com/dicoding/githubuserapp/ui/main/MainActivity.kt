package com.dicoding.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.*
import com.dicoding.githubuserapp.data.local.datastore.SettingPreferences
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.helper.SettingViewModelFactory
import com.dicoding.githubuserapp.ui.adapter.UsersAdapter
import com.dicoding.githubuserapp.ui.favorite.FavoriteActivity
import com.dicoding.githubuserapp.ui.setting.SettingActivity
import com.dicoding.githubuserapp.ui.setting.SettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Search User"

        val settingPreferences = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(settingPreferences))[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        initObserver()
        initSearch()
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
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) {
            showError(it)
        }

        mainViewModel.isFound.observe(this) {
            showNotFound(it)
        }

        mainViewModel.users.observe(this) { users ->
            if (users != null) {
                setUsers(users)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.errorMessage.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showNotFound(isFound: Boolean) {
        binding.notFound.visibility = if (isFound) View.VISIBLE else View.GONE
    }

    private fun setUsers(users: ArrayList<UsersItem>) {
        binding.rvUsers.adapter = UsersAdapter(users)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun initSearch() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_user)

        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        mainViewModel.findUsers(query)
                    }

                    searchView.clearFocus()

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        mainViewModel.getUsers()
                    }

                    return false
                }
            }
        )
    }
}