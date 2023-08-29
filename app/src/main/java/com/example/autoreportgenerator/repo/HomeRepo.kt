package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.model.ScanRequest
import com.example.autoreportgenerator.service.RetrofitService



class HomeRepo constructor(private val retrofitService: RetrofitService) {

    suspend fun postScan(headers: Map<String, String>, model: ScanRequest): RegisterResponse =
        retrofitService.postScan(headers, model).body()!!

    suspend fun genReport(headers: Map<String, String>) =
        retrofitService.fetchReport(headers).body()!!

}