package com.dicoding.githubuserapp.view.activity

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.model.UsersItem
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.view.adapter.SectionsPagerAdapter
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.dicoding.githubuserapp.model.UserResponse
import com.dicoding.githubuserapp.viewmodel.DetailViewModel
import com.dicoding.githubuserapp.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val KEY_GITHUB_USER = "key_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val githubUserData = intent.getParcelableExtra(KEY_GITHUB_USER) as UsersItem?

        if (githubUserData != null) {
            val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
            detailViewModel.getUser(githubUserData.login)
            detailViewModel.user.observe(this) { user ->
                run {
                    setUserData(user)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    private fun setUserData(user: UserResponse) {
        Glide.with(binding.civGithubUserDetail).load(user.avatarUrl).into(findViewById(R.id.civ_github_user_detail))
        findViewById<TextView>(R.id.tv_github_name_detail).text = user.name
        findViewById<TextView>(R.id.tv_github_username_detail).text = user.login
        findViewById<TextView>(R.id.tv_github_bio_detail).text = user.bio
        findViewById<TextView>(R.id.tv_github_location_detail).text = user.location
        findViewById<TextView>(R.id.tv_github_repositories_detail).text = user.publicRepos.toString()
        findViewById<TextView>(R.id.tv_github_followers_detail).text = user.followers.toString()
    }
}