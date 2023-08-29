package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class RegistrationUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("isRoleTypeDoctor")
    val isRoleTypeDoctor: Boolean
)