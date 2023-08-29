package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class LoginValidateResults (
    @SerializedName("user") var loginUserRes         : LoginUserRes?         = LoginUserRes(),
    @SerializedName("token" ) var token : String? = null
)