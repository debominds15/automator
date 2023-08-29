package com.example.autoreportgenerator

import com.example.autoreportgenerator.model.ScanResults
import com.google.gson.annotations.SerializedName


data class ScanResponse (

    @SerializedName("message" )
    var message : String?  = null,
    @SerializedName("error"   )
    var error   : Boolean? = null,
    @SerializedName("code"    )
    var code    : Int?     = null,
    @SerializedName("results" )
    var results : ScanResults? = ScanResults()

)