package com.dicoding.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GithubUsersAdapter
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)

        adapter = GithubUsersAdapter(arrayListOf())

        binding.rvGithubUsers.layoutManager = layoutManager
        binding.rvGithubUsers.adapter = adapter
        binding.rvGithubUsers.setHasFixedSize(true)

        getGithubUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    private fun getGithubUsers() {
        val client = ApiConfig.getApiService().getGithubUsers()

        client.enqueue(object : Callback<ArrayList<GithubUsersResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<GithubUsersResponseItem>>,
                response: Response<ArrayList<GithubUsersResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        setGithubUsersData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GithubUsersResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGithubUsersData(githubUsers: ArrayList<GithubUsersResponseItem>) {
        adapter.setData(githubUsers)
    }
}