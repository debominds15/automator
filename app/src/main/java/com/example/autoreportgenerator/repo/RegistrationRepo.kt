package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.service.Resource
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.NetworkUtil.getErrorResponse


class RegistrationRepo constructor(private val retrofitService: RetrofitService) {

    suspend fun postRegistration(registerModel: RegistrationUser): Resource<RegisterResponse> {
        val response = retrofitService.submitRegistration(registerModel)
        val result = response.body()

        return try {

            //Handling api success responses (201, 200)
            if (response.isSuccessful && result != null) {

                Resource.Success(result)

            } else {
                //Handling api error response (501, 404)
                Resource.ErrorRes(getErrorResponse(response.errorBody()!!.string()))

            }
        } catch (e: Exception) {
            //This is for handling errors where we sometimes misconfigured retrofit instance
            Resource.Error(e.message ?: "An error occured")
        }
    }


    suspend fun validateRegistration(token: String): Resource<RegisterResponse> {
        //retrofitService.verifyRegistration(token).body()?: RegisterResponse("Something went wrong! Please try again!")

        val response = retrofitService.verifyRegistration(token)
        val result = response.body()

        return try {

            //Handling api success responses (201, 200)
            if (response.isSuccessful && result != null) {

                Resource.Success(result)

            } else {
                //Handling api error response (501, 404)
                Resource.ErrorRes(getErrorResponse(response.errorBody()!!.string()))

            }
        } catch (e: Exception) {
            //This is for handling errors where we sometimes misconfigured retrofit instance
            Resource.Error(e.message ?: "An error occured")
        }
    }
}