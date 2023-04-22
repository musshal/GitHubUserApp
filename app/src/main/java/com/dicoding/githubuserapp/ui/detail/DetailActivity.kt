package com.dicoding.githubuserapp.ui.detail

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
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.dicoding.githubuserapp.data.remote.response.UserResponse
import com.dicoding.githubuserapp.helper.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        FavoriteViewModelFactory.getInstance(
            application
        )
    }
    private lateinit var favoriteUser: FavoriteUser

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra(EXTRA_USER) as UsersItem?

        if (user != null) {
            val sectionsPagerAdapter = SectionsPagerAdapter(this, user.login)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            val tabs: TabLayout = findViewById(R.id.tabs)

            viewPager.adapter = sectionsPagerAdapter

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (user != null) {
            initObserver(user)
        }

        binding.fabFavorite.apply {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun initObserver(user: UsersItem) {
        detailViewModel.getUser(user.login)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isError.observe(this) {
            showError(it)
        }

        detailViewModel.user.observe(this) {
            if (it != null) {
                setUserData(it)
                favoriteUser = FavoriteUser(it.login, it.avatarUrl)
                showFavoriteButton(true)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.errorMessage.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showFavoriteButton(isLoading: Boolean) {
        binding.fabFavorite.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(user: UserResponse) {
        with(binding) {
            Glide
                .with(this.root.context)
                .load(user.avatarUrl)
                .into(findViewById(R.id.civ_user_detail))

            findViewById<TextView>(R.id.tv_name_detail).text = user.name
            findViewById<TextView>(R.id.tv_username_detail).text = user.login
            findViewById<TextView>(R.id.tv_bio_detail).text = "Bio: ${user.bio}"
            findViewById<TextView>(R.id.tv_location_detail).text = "Loc: ${user.location}"
            findViewById<TextView>(R.id.tv_repositories_detail).text = "Repos: ${user.publicRepos}"
            findViewById<TextView>(R.id.tv_following_detail).text = "Follower: ${user.following}"
            findViewById<TextView>(R.id.tv_followers_detail).text = "Following: ${user.followers}"
        }

        favoriteUser = FavoriteUser(user.login, user.avatarUrl)
        detailViewModel.isFavorited(favoriteUser.login).observe(this) {
            setFavoriteUser(it)
        }
    }

    private fun setFavoriteUser(value: Boolean) {
        binding.fabFavorite.apply {
            if (value) {
                tag = "favorite"
                setImageDrawable(ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.baseline_favorite_24
                ))
            } else {
                tag = ""
                setImageDrawable(ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.baseline_favorite_border_24
                ))
            }
        }
    }
}