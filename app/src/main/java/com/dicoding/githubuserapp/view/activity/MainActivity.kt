package com.dicoding.githubuserapp.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.*
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.model.*
import com.dicoding.githubuserapp.view.adapter.UsersAdapter
import com.dicoding.githubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Github Search User")

        val layoutManager = LinearLayoutManager(this)

        adapter = UsersAdapter(arrayListOf())

        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.adapter = adapter
        binding.rvUsers.setHasFixedSize(true)

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

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
            setUsersData(users)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_user)

        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.findUsers(query)
                    mainViewModel.users.observe(this@MainActivity) { users ->
                        setUsersData(users)
                    }
                }

                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    mainViewModel.getUsers()
                    mainViewModel.users.observe(this@MainActivity) { users ->
                        setUsersData(users)
                    }
                }

                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }

    private fun setUsersData(users: ArrayList<UsersItem>) {
        adapter.setData(users)
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
}