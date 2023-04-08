package com.dicoding.githubuserapp.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.*
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.model.*
import com.dicoding.githubuserapp.view.adapter.UsersAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)

        adapter = UsersAdapter(arrayListOf())

        binding.rvGithubUsers.layoutManager = layoutManager
        binding.rvGithubUsers.adapter = adapter
        binding.rvGithubUsers.setHasFixedSize(true)

        getUsers()

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_github_user)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    findUsers(query)
                }

                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    getUsers()
                }

                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    private fun getUsers() {
        val client = ApiConfig.getApiService().getUsers()

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
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getUser(username: String) {
        val client = ApiConfig.getApiService().getUser(username)

        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        getUserData(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun findUsers(query: String) {
        val client = ApiConfig.getApiService().findUsers(query)

        client.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        setUsersData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUsersData(users: ArrayList<UsersItem>) {
        adapter.setData(users)
    }

    private fun getUserData(user: UserResponse) : UserResponse {
        return user
    }
}