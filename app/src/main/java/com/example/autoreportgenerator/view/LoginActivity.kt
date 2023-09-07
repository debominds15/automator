package com.example.autoreportgenerator.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.LoginViewModelFactory
import com.example.autoreportgenerator.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity()  {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val layoutLogin: RelativeLayout = findViewById(R.id.layout_login)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val regButton: Button = findViewById(R.id.registerButton)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val tvError: TextView = findViewById(R.id.tv_error)

        val retrofitService = RetrofitService.getInstance()
        val regRepository = LoginRepo(retrofitService)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(regRepository)
        )[LoginViewModel::class.java]

        loginButton.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            if(tvError.visibility == View.VISIBLE)
                tvError.visibility = View.GONE
            val loginRequest = LoginUser(
                email = usernameEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
            viewModel.login(loginRequest)
        }

        regButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        viewModel.loginResponseState.observe(this, Observer {
            progressBar.visibility = View.GONE
            if(it["response"] == "success"){
                val bundle = Bundle()
                bundle.apply {
                    putString("token", it["token"])
                    putString("name", it["name"])
                    putString("userId", it["userId"])
                    putBoolean("isDoctor", it["isDoctor"] == "doctor")
                }
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loginApiErrorState.observe(this) { message ->
            progressBar.visibility = View.GONE
            tvError.visibility = View.VISIBLE
            tvError.text = message
        }
//        viewModel.loginState.observe(this, { state ->
//            Toast.makeText(this, state, Toast.LENGTH_SHORT).show()
//
//        })
    }
}