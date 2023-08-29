package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class RegisterResponse (

    @SerializedName("message" )
    var message : String?  = null,
    @SerializedName("error"   )
    var error   : Boolean? = null,
    @SerializedName("code"    )
    var code    : Int?     = null,
    @SerializedName("results" )
    var results : Results? = Results()

)