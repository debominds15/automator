package com.example.autoreportgenerator.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.LoginViewModelFactory
import com.example.autoreportgenerator.utils.MyViewModelFactory
import com.example.autoreportgenerator.viewmodel.LoginViewModel
import com.example.autoreportgenerator.viewmodel.RegistrationViewModel

class LoginActivity : AppCompatActivity()  {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val regButton: Button = findViewById(R.id.registerButton)

        val retrofitService = RetrofitService.getInstance()
        val regRepository = LoginRepo(retrofitService)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(regRepository)
        )[LoginViewModel::class.java]

        loginButton.setOnClickListener{
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
        viewModel.loginState.observe(this, Observer {
            if(it){
              Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show()
            }
        })
//        viewModel.loginState.observe(this, { state ->
//            Toast.makeText(this, state, Toast.LENGTH_SHORT).show()
//
//        })
    }
}