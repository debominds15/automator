package com.example.autoreportgenerator.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity()  {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.loginButton)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        registerButton.setOnClickListener {
            val user = LoginUser(
                username = usernameEditText.text.toString(),
                password = passwordEditText.text.toString(),
            )
            viewModel.login(user)
        }

        viewModel.loginState.observe(this, { state ->
            Toast.makeText(this, state, Toast.LENGTH_SHORT).show()
        })
    }
}