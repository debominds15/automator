package com.example.autoreportgenerator.view

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.viewmodel.HomeViewModel


class HomeActivity : AppCompatActivity() {
    /*private lateinit var viewModel: HomeViewModel
    private lateinit var token: String
    private lateinit var userId: String
    private var isDoctor = false*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val homeAdapter = ViewPagerAdapter(this, 2)
        viewPager.adapter = homeAdapter
    }
}