package com.dicoding.githubuserapp.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuserapp.ui.fragment.FollowerFragment
import com.dicoding.githubuserapp.ui.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private var username: String) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }

        if (fragment != null) {
            fragment.arguments = Bundle().apply {
                putString(FollowingFragment.USERNAME, username)
            }
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}