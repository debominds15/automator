package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class LoginResults (
    @SerializedName("token" ) var token : String? = null
)