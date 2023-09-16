package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.ErrorResponse
import com.example.autoreportgenerator.model.LoginResponse
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.service.Resource
import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepo) : ViewModel() {

    var token = ""
    val loginEvent = MutableLiveData<LoginEvent<Any>>()


    var job: Job? = null

    sealed class LoginEvent<out T : Any> {

        class Success<out T : Any>(val loginResponse: T) : LoginEvent<T>()
        class ErrorResponseResult(val errorResponse: ErrorResponse) : LoginEvent<Nothing>()
        class Failure(val message: String?) : LoginEvent<Nothing>()
        object Loading : LoginEvent<Nothing>()
        object Empty : LoginEvent<Nothing>()

    }

    private fun getHeaderMap(token: String): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }

    fun login(model: LoginUser) {
        loginEvent.value = LoginEvent.Loading
        job = CoroutineScope(Dispatchers.IO).launch {
            if (model.email.trim().isEmpty() || model.password.trim().isEmpty()) {
                loginEvent.value = LoginEvent.Failure("Please fill in all fields")
            } else {
                val response: Resource<LoginResponse> = loginRepository.postLogin(model)
                withContext(Dispatchers.Main) {
                    when (response) {

                        is Resource.Success -> {
                            loginEvent.value =
                                LoginEvent.Success(response.data!!)
                        }

                        is Resource.ErrorRes -> {

                            loginEvent.value =
                                LoginEvent.ErrorResponseResult(
                                    response.errorResponse
                                        ?: ErrorResponse(message = "Something went wrong, please try again!")
                                )

                        }

                        is Resource.Error -> {
                            loginEvent.value =
                                LoginEvent.Failure(response.message)

                        }
                        else -> {

                        }
                    }
                }
            }
            //ToDO: Call Login API here
            //ToDo: Move to Home Activity
        }
    }

    fun performAuth(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseValidate = loginRepository.validateLogin(getHeaderMap(token))
            withContext(Dispatchers.Main) {
                when (responseValidate) {

                    is Resource.Success -> {
                        loginEvent.value = LoginEvent.Success(responseValidate.data!!)
                    }

                    is Resource.ErrorRes -> {

                        loginEvent.value = LoginEvent.ErrorResponseResult(
                            responseValidate.errorResponse
                                ?: ErrorResponse(message = "Something went wrong, please try again!")
                        )

                    }

                    is Resource.Error -> {
                        loginEvent.value = LoginEvent.Failure(responseValidate.message)

                    }
                    else -> {

                    }
                }
            }
        }
    }
}