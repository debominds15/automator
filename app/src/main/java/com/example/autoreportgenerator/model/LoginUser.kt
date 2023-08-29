package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class LoginUser (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)