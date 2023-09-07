package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class File(

    @SerializedName("pdfName") var pdfName: String? = null,
    @SerializedName("pdfUrl") var pdfUrl: String? = null,
    @SerializedName("pdfId") var pdfId: String? = null

)