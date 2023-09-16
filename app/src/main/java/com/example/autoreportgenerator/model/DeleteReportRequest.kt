package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class DeleteReportRequest(
    @SerializedName("publicId")
    val publicId: String
)