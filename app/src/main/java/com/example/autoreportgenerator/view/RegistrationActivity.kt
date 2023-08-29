package com.example.autoreportgenerator.view

import android.content.Intent
import com.example.autoreportgenerator.viewmodel.RegistrationViewModel
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.MyViewModelFactory


class RegistrationActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val mobileNumberEditText: EditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)
        val roleSpinner: Spinner = findViewById(R.id.roleSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            roleSpinner.adapter = adapter
        }

        val retrofitService = RetrofitService.getInstance()
        val regRepository = RegistrationRepo(retrofitService)
        //viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        viewModel = ViewModelProvider(this,
            MyViewModelFactory(regRepository))[RegistrationViewModel::class.java]


        registerButton.setOnClickListener {
            val user = RegistrationUser(
                name = usernameEditText.text.toString(),
                email = emailEditText.text.toString(),
                password = passwordEditText.text.toString(),
                isRoleTypeDoctor = roleSpinner.selectedItem.toString() == "Doctor"
            )
            viewModel.register(user)
        }
        /*viewModel.registrationState.observe(this, { state ->
            Toast.makeText(this, state, Toast.LENGTH_SHORT).show()
        })*/

        viewModel.registrationState.observe(this, Observer {
            if(it){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        })
    }
}