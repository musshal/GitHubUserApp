package com.dicoding.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchEditText: TextInputEditText
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Github Search User"

        initTheme()
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

    private fun initTheme() {
        val settingPreferences = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(settingPreferences))[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
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
        searchEditText = findViewById(R.id.edReview)
        val searchLayout = findViewById<TextInputLayout>(R.id.inputLayout)

        searchLayout.setEndIconOnClickListener {
            searchEditText.text?.clear()
        }

        val rootView = window.decorView.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardShowing = false

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                isKeyboardShowing = if (keypadHeight > screenHeight * 0.15) {
                    true
                } else {
                    if (isKeyboardShowing) {
                        searchEditText.clearFocus()
                    }
                    false
                }
            }
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val userInput = searchEditText.text.toString()
                onSubmit(userInput)
                return@setOnEditorActionListener true
            }

            false
        }
    }

    private fun onSubmit(userInput: String) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (userInput != "") {
            mainViewModel.findUsers(userInput)
            searchEditText.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0) // hide keyboard after search operation
        } else {
            mainViewModel.getUsers()
            searchEditText.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0) // hide keyboard after search operation
        }
    }
}