package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.repo.RegistrationRepo
import kotlinx.coroutines.*

class RegistrationViewModel(private val regRepository: RegistrationRepo) : ViewModel() {

    val registrationState = MutableLiveData<Boolean>()

    var job: Job? = null

    fun register(model: RegistrationUser) {

        /*if (user.username.trim().isEmpty() || user.email.trim().isEmpty() || user.password.trim().isEmpty() || user.mobileNumber.trim().isEmpty()) {
            registrationState.value = "Please fill in all fields"
        } else {
            registrationState.value = "Registered successfully"
            //ToDO: Call Registration API here
            //ToDo: move to LoginActivity
        }*/

        job = CoroutineScope(Dispatchers.IO).launch {
            val response: RegisterResponse = regRepository.postRegistration(model)
            withContext(Dispatchers.Main) {
                if (response.code == 201) {
                    val token = response.results?.verification?.token ?: ""
                    val responseValidate = regRepository.validateRegistration(token)
                    if (responseValidate.code == 200)
                        registrationState.postValue(true)
                    else {
                        registrationState.postValue(false)
                        //loading.value = false
                    }
                } else {
                    registrationState.postValue(false)
                    //onError("Error : ${response.message()} ")
                }
            }


        }
    }

}