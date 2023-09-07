package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class FileReportResponse(

    @SerializedName("message")
    var message: String? = null,
    @SerializedName("error")
    var error: Boolean? = null,
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("results")
    var loginresults: FileResults? = null

)