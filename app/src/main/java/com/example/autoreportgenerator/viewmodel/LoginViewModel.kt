package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.LoginResponse
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.repo.LoginRepo
import kotlinx.coroutines.*

class LoginViewModel(private val regRepository: LoginRepo) : ViewModel() {

    val loginApiErrorState = MutableLiveData<String>()
    val loginResponseState = MutableLiveData<HashMap<String, String>>()

    var job: Job? = null

    private fun getHeaderMap(token: String): Map<String, String> {
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
            if (response.code == 200) {
                //loginState.postValue(true)
                val token = response.loginresults?.token ?: ""
                val responseValidate = regRepository.validateLogin(getHeaderMap(token))
                withContext(Dispatchers.Main) {
                    val hashMap: HashMap<String, String> = HashMap()
                    if (responseValidate.code == 200) {
                        hashMap["name"] =
                            responseValidate.loginresults?.loginUserRes?.name ?: "User"
                        hashMap["token"] = token
                        hashMap["userId"] = responseValidate.loginresults?.loginUserRes?.Id ?: "id"
                        hashMap["isDoctor"] =
                            if (responseValidate.loginresults?.loginUserRes?.isRoleTypeDoctor == true) "doctor" else "patient"
                        hashMap["response"] = "success"
                    } else {
                        hashMap["response"] = "fail"
                    }
                    loginResponseState.postValue(hashMap)
                }
            } else {
                loginApiErrorState.postValue("Something went wrong! Please try again.")
            }
        }
    }
}