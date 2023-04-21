package com.dicoding.githubuserapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.data.remote.response.UsersItem
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.ui.adapter.SectionsPagerAdapter
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.dicoding.githubuserapp.data.remote.response.UserResponse
import com.dicoding.githubuserapp.helper.ViewModelFactory
import com.dicoding.githubuserapp.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteUser: FavoriteUser

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val KEY_USER = "key_user"
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME).toString()
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)

        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        supportActionBar?.setTitle("Detail User")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val userData = intent.getParcelableExtra(KEY_USER) as UsersItem?
        detailViewModel = obtainViewModel(this@DetailActivity)
        if (userData != null) {
            detailViewModel.getUser(userData.login)

            detailViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            detailViewModel.isError.observe(this) {
                showError(it)
            }

            detailViewModel.user.observe(this) {
                setUserData(it)
                favoriteUser = FavoriteUser(it.login, it.avatarUrl)
            }
        }

        binding.fabAdd.apply {
            setOnClickListener {
                if (tag == "favorite") {
                    detailViewModel.delete(favoriteUser)
                } else {
                    detailViewModel.insert(favoriteUser)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun setFavoriteUser(value: Boolean) {
        binding.fabAdd.apply {
            if (value) {
                tag = "favorite"
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.baseline_favorite_24
                    )
                )
            } else {
                tag = ""
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.baseline_favorite_border_24
                    )
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.menu, menu)

        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(user: UserResponse) {
        Glide
            .with(binding.civUserDetail)
            .load(user.avatarUrl)
            .into(findViewById(R.id.civ_user_detail))

        findViewById<TextView>(R.id.tv_name_detail).text = user.name
        findViewById<TextView>(R.id.tv_username_detail).text = user.login
        findViewById<TextView>(R.id.tv_bio_detail).text = "Bio: ${user.bio}"
        findViewById<TextView>(R.id.tv_location_detail).text = "Loc: ${user.location}"
        findViewById<TextView>(R.id.tv_repositories_detail).text = "Repos: ${user.publicRepos}"
        findViewById<TextView>(R.id.tv_following_detail).text = "Follower: ${user.following}"
        findViewById<TextView>(R.id.tv_followers_detail).text = "Following: ${user.followers}"

        favoriteUser = FavoriteUser(user.login, user.avatarUrl)
        detailViewModel.isFavorited(user.login).observe(this) {
            setFavoriteUser(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.errorMessage.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)

        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
}