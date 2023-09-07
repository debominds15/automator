package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class LoginResponse (

    @SerializedName("message" )
    var message : String?  = null,
    @SerializedName("error"   )
    var error   : Boolean? = null,
    @SerializedName("code"    )
    var code    : Int?     = null,
    @SerializedName("results" )
    var loginresults : LoginResults? = LoginResults()/*,
    @SerializedName("errors" )
    var errors : String? = null*/

)