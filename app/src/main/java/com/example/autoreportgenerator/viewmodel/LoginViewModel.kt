package com.example.autoreportgenerator.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.LoginResponse
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.repo.RegistrationRepo
import kotlinx.coroutines.*

class LoginViewModel(private val regRepository: LoginRepo) : ViewModel() {

    val loginState = MutableLiveData<Boolean>()

    var job: Job? = null

    private fun getHeaderMap(token: String): Map<String, String> {
        //val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNjRlZDkxZWJmN2YyYmM4ZjQwOTI1MDAwIiwibmFtZSI6IkRlYm9qeW90aSBCYW5payIsImVtYWlsIjoiYWJjZDI1MEBnbWFpbC5jb20ifSwiaWF0IjoxNjkzMjkxMDM5LCJleHAiOjE2OTMyOTQ2Mzl9.UyNnthZie3NbHJx4Z7WGvWtQLobSEUTrau1LP9VrRQ8"
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }

    fun login(model: LoginUser) {
//        if (user.username.trim().isEmpty() || user.password.trim().isEmpty()) {
//            loginState.value = "Please fill in all fields"
//        } else {
//            loginState.value = "login successfully"
//            //ToDO: Call Login API here
//            //ToDo: Move to Home Activity
// }
        job = CoroutineScope(Dispatchers.IO).launch {
            val response: LoginResponse = regRepository.postLogin(model)
            withContext(Dispatchers.Main) {
                if (response.code == 200) {
                    val token = response.loginresults?.token ?: ""
                    val responseValidate = regRepository.validateLogin(getHeaderMap(token))
                    Log.d("VAMSI",responseValidate.code.toString())
                    if (responseValidate.code == 200)
                        loginState.postValue(true)
                    else {
                        loginState.postValue(false)
                        //loading.value = false
                    }
                } else {
                    loginState.postValue(false)
                    //onError("Error : ${response.message()} ")
                }
            }
        }
    }
}