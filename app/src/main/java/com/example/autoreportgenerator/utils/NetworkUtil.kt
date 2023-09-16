package com.example.autoreportgenerator.utils

import android.content.Context
import com.example.autoreportgenerator.model.ErrorResponse
import com.google.gson.Gson

object NetworkUtil : InternetConnectionCallback {
    var isNetworkConnected = false

    fun registerConnectionCallback(context: Context) {
        InternetConnectionObserver
            .instance(context)
            .setCallback(this)
            .register()
    }

    fun unRegisterConnectionCallback() {
        InternetConnectionObserver.unRegister()
    }

    override fun onConnected() {
        isNetworkConnected = true
    }

    override fun onDisconnected() {
        isNetworkConnected = false
    }

    fun getErrorResponse(response: String): ErrorResponse {
        return Gson().fromJson(response, ErrorResponse::class.java)
    }

}