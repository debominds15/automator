package com.example.autoreportgenerator.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.autoreportgenerator.R

class SplashActivity : AppCompatActivity() {

    private var progressStatus = 0
    private lateinit var progressBarHorizontal: ProgressBar
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressBarHorizontal = findViewById(R.id.progressBarHorizontal)
        simulateProgress()
    }

    private fun simulateProgress() {
        val runnable = Runnable {
            if (progressStatus < 100) {
                progressStatus++
                progressBarHorizontal.progress = progressStatus
                handler.postDelayed(this::simulateProgress, 50) // Update every 50 milliseconds
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        handler.post(runnable)
    }
}