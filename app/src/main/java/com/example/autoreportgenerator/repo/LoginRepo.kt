package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.service.RetrofitService
import retrofit2.http.HeaderMap

class LoginRepo  constructor(private val retrofitService: RetrofitService) {

    suspend fun validateLogin(@HeaderMap headers: Map<String, String>): LoginValidateResponse =
        retrofitService.verifyLogin(headers).body()!!

    suspend fun postLogin(loginModel: LoginUser): LoginResponse =
        retrofitService.submitLogin(loginModel).body()!!

}