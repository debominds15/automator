package com.example.autoreportgenerator.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: AppCompatActivity, val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return HomeFragment.getInstance()
            }
            1 -> {
                return ViewerFragment.getInstance()
            }
            else -> {
                return HomeFragment.getInstance()
            }
        }

        return HomeFragment.getInstance()
    }
}
