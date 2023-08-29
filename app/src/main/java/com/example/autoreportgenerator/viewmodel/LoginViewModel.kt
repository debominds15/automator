package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.RegistrationUser

class LoginViewModel : ViewModel() {

    val loginState = MutableLiveData<String>()

    fun login(user: LoginUser) {
        if (user.username.trim().isEmpty() || user.password.trim().isEmpty()) {
            loginState.value = "Please fill in all fields"
        } else {
            loginState.value = "login successfully"
            //ToDO: Call Login API here
            //ToDo: Move to Home Activity
        }
    }
}