package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.LoginResponse
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.LoginValidateResponse
import com.example.autoreportgenerator.service.Resource
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.NetworkUtil
import retrofit2.http.HeaderMap

class LoginRepo constructor(private val retrofitService: RetrofitService) {

    suspend fun validateLogin(@HeaderMap headers: Map<String, String>): Resource<LoginValidateResponse> {
        val response = retrofitService.verifyLogin(headers)
        val result = response.body()

        return try {

            //Handling api success responses (201, 200)
            if (response.isSuccessful && result != null) {

                Resource.Success(result)

            } else {
                //Handling api error response (501, 404)
                Resource.ErrorRes(NetworkUtil.getErrorResponse(response.errorBody()!!.string()))

            }
        } catch (e: Exception) {
            //This is for handling errors where we sometimes misconfigured retrofit instance
            Resource.Error(e.message ?: "An error occured")
        }
    }

    suspend fun postLogin(loginModel: LoginUser): Resource<LoginResponse> {
        val response = retrofitService.submitLogin(loginModel)
        val result = response.body()

        return try {

            //Handling api success responses (201, 200)
            if (response.isSuccessful && result != null) {

                Resource.Success(result)

            } else {
                //Handling api error response (501, 404)
                Resource.ErrorRes(NetworkUtil.getErrorResponse(response.errorBody()!!.string()))

            }
        } catch (e: Exception) {
            //This is for handling errors where we sometimes misconfigured retrofit instance
            Resource.Error(e.message ?: "An error occured")
        }
    }
}