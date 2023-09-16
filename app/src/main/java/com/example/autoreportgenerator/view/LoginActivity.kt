package com.example.autoreportgenerator.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.LoginResponse
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.LoginValidateResponse
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.LoginViewModelFactory
import com.example.autoreportgenerator.utils.NetworkUtil
import com.example.autoreportgenerator.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    @RequiresApi(Build.VERSION_CODES.M)
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



        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(regRepository)
        )[LoginViewModel::class.java]

        loginButton.setOnClickListener {
            if (progressBar.visibility == View.GONE)
                progressBar.visibility = View.VISIBLE
            if (tvError.visibility == View.VISIBLE)
                tvError.visibility = View.GONE
            if (NetworkUtil.isNetworkConnected) {
                val loginRequest = LoginUser(
                    email = usernameEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
                viewModel.login(loginRequest)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@LoginActivity,
                    "Please check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        regButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        viewModel.loginEvent.observe(this) { response ->

            when (response) {

                is LoginViewModel.LoginEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is LoginViewModel.LoginEvent.Success -> {
                    progressBar.visibility = View.GONE
                    val result = response
                    when (response.loginResponse) {
                        is LoginResponse -> {
                            val token = response.loginResponse.loginresults?.token ?: ""
                            viewModel.token = token
                            viewModel.performAuth(
                                token
                            )
                        }
                        is LoginValidateResponse -> {
                            val bundle = Bundle()
                            bundle.apply {
                                putString("token", viewModel.token)
                                putString(
                                    "name",
                                    response.loginResponse.loginresults?.loginUserRes?.name ?: ""
                                )
                                putString(
                                    "userId",
                                    response.loginResponse.loginresults?.loginUserRes?.Id ?: ""
                                )
                                putBoolean(
                                    "isDoctor",
                                    response.loginResponse.loginresults?.loginUserRes?.isRoleTypeDoctor
                                        ?: false
                                )
                            }
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                }
                is LoginViewModel.LoginEvent.ErrorResponseResult -> {
                    //We are doing our preferred stuff which we want in case of error response
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text =
                        response.errorResponse.errors?.msg ?: response.errorResponse.message
                }

                is LoginViewModel.LoginEvent.Failure -> {
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = response.message
                }
                else -> {
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = "Please try again!"
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        NetworkUtil.registerConnectionCallback(this)
    }

    override fun onPause() {
        super.onPause()
        NetworkUtil.unRegisterConnectionCallback()
    }
}