package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.service.RetrofitService
import retrofit2.Response


class RegistrationRepo constructor(private val retrofitService: RetrofitService) {

    suspend fun postRegistration(registerModel: RegistrationUser): RegisterResponse =
        retrofitService.submitRegistration(registerModel).body()!!

    suspend fun validateRegistration(token: String): RegisterResponse =
        retrofitService.verifyRegistration(token).body()!!

}