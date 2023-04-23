package com.dicoding.githubuserapp.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserapp.data.local.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val settingPreferences: SettingPreferences): ViewModel() {
    fun getThemeSetting() : LiveData<Boolean> = settingPreferences.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkModeActive)
        }
    }
}