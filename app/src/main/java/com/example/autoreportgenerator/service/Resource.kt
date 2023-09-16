package com.example.autoreportgenerator.service

import com.example.autoreportgenerator.model.ErrorResponse

sealed class Resource<T>(
    val loading: Boolean? = false,
    val data: T? = null,
    val message: String? = null,
    val errorResponse: ErrorResponse?
) {

    // We'll wrap our data in this 'Success'
    // class in case of success response from api
    class Success<T>(data: T) : Resource<T>(false, data = data, null, null)

    // We'll pass error message wrapped in this 'Error'
    // class to the UI in case of failure response
    class Error<T>(errorMessage: String) : Resource<T>(false, null, message = errorMessage, null)

    class ErrorRes<T>(errorResponse: ErrorResponse?) : Resource<T>(false, null, null, errorResponse)


    // We'll just pass object of this Loading
    // class, just before making an api call
    class Loading<T>(loading: Boolean) : Resource<T>(loading, null, null, null)
}