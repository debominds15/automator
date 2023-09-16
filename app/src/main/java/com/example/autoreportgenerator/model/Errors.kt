package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("msg") var msg: String? = null
)