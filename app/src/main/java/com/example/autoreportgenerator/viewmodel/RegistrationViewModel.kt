package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.ErrorResponse
import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.service.Resource
import kotlinx.coroutines.*

class RegistrationViewModel(private val regRepository: RegistrationRepo) : ViewModel() {

    val registrationEvent = MutableLiveData<RegisterEvent>()
    val registrationVerificationEvent = MutableLiveData<RegisterEvent>()
    var token: String = ""
    var isRoleTypeSelected = false

    var job: Job? = null

    sealed class RegisterEvent {

        class Success(val registerResponse: RegisterResponse) : RegisterEvent()
        class ErrorResponseResult(val errorResponse: ErrorResponse) : RegisterEvent()
        class Failure(val message: String?) : RegisterEvent()
        object Loading : RegisterEvent()
        object Empty : RegisterEvent()

    }

    fun register(model: RegistrationUser) {
        registrationEvent.value = RegisterEvent.Loading
        job = CoroutineScope(Dispatchers.IO).launch {
            if (model.name.trim().isEmpty() || model.email.trim().isEmpty() || model.password.trim()
                    .isEmpty() || !isRoleTypeSelected
            ) {
                registrationEvent.value = RegisterEvent.Failure("Please fill in all fields")
            } else {


                val response: Resource<RegisterResponse> = regRepository.postRegistration(model)
                withContext(Dispatchers.Main) {
                    when (response) {

                        is Resource.Success -> {
                            registrationEvent.value = RegisterEvent.Success(response.data!!)
                        }

                        is Resource.ErrorRes -> {

                            registrationEvent.value = RegisterEvent.ErrorResponseResult(
                                response.errorResponse
                                    ?: ErrorResponse(message = "Something went wrong, please try again!")
                            )

                        }

                        is Resource.Error -> {
                            registrationEvent.value = RegisterEvent.Failure(response.message)

                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    fun validateRegister(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseValidate = regRepository.validateRegistration(token)
            withContext(Dispatchers.Main) {
                when (responseValidate) {

                    is Resource.Success -> {
                        registrationVerificationEvent.value =
                            RegisterEvent.Success(responseValidate.data!!)
                    }

                    is Resource.ErrorRes -> {

                        registrationVerificationEvent.value = RegisterEvent.ErrorResponseResult(
                            responseValidate.errorResponse
                                ?: ErrorResponse(message = "Something went wrong, please try again!")
                        )

                    }

                    is Resource.Error -> {
                        registrationVerificationEvent.value =
                            RegisterEvent.Failure(responseValidate.message)

                    }
                    else -> {

                    }
                }
            }
        }
    }

}