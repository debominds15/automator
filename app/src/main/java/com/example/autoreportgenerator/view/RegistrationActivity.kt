package com.example.autoreportgenerator.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.MyViewModelFactory
import com.example.autoreportgenerator.utils.NetworkUtil
import com.example.autoreportgenerator.viewmodel.RegistrationViewModel


class RegistrationActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationViewModel
    private var roleType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        init()
    }

    fun init() {

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)
        val roleSpinner = findViewById<Spinner>(R.id.role_spinner)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val tvError: TextView = findViewById(R.id.tv_error)

        val data = arrayOf("Select your role", "Doctor", "Patient")

        val adapter: ArrayAdapter<*> =
            ArrayAdapter<String>(this, R.layout.item_selected_spinner, data)
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner)


        roleSpinner.adapter = adapter
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                roleType = parent.getItemAtPosition(position).toString()
                if (roleType != "Select your role") {
                    viewModel.isRoleTypeSelected = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val retrofitService = RetrofitService.getInstance()
        val regRepository = RegistrationRepo(retrofitService)
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(regRepository)
        )[RegistrationViewModel::class.java]




        registerButton.setOnClickListener {

            if (progressBar.visibility == View.GONE)
                progressBar.visibility = View.VISIBLE
            if (tvError.visibility == View.VISIBLE)
                tvError.visibility = View.GONE
            if (NetworkUtil.isNetworkConnected) {
                val userRegistration = RegistrationUser(
                    name = usernameEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString(),
                    isRoleTypeDoctor = roleSpinner.selectedItem.toString() == "Doctor"
                )
                viewModel.register(userRegistration)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@RegistrationActivity,
                    "Please check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        /*viewModel.registrationState.observe(this, { state ->
            Toast.makeText(this, state, Toast.LENGTH_SHORT).show()
        })*/

        viewModel.registrationEvent.observe(this) { response ->

            when (response) {

                is RegistrationViewModel.RegisterEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is RegistrationViewModel.RegisterEvent.Success -> {
                    progressBar.visibility = View.GONE
                    viewModel.validateRegister(
                        response.registerResponse.results?.verification?.token ?: ""
                    )
                }
                is RegistrationViewModel.RegisterEvent.ErrorResponseResult -> {
                    //We are doing our preferred stuff which we want in case of error response
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text =
                        response.errorResponse.errors?.msg ?: response.errorResponse.message
                }

                is RegistrationViewModel.RegisterEvent.Failure -> {
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

        viewModel.registrationVerificationEvent.observe(this) { response ->

            when (response) {

                is RegistrationViewModel.RegisterEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is RegistrationViewModel.RegisterEvent.Success -> {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                is RegistrationViewModel.RegisterEvent.ErrorResponseResult -> {
                    //We are doing our preferred stuff which we want in case of error response
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = response.errorResponse.errors?.msg
                }

                is RegistrationViewModel.RegisterEvent.Failure -> {
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