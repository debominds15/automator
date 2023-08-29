package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class LoginValidateResults (
    @SerializedName("user") var loginuserRes         : LoginUserRes?         = LoginUserRes(),
    @SerializedName("verification" ) var verification : Verification? = Verification(),
    @SerializedName("token" ) var token : String? = null
)