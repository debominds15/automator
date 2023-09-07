package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class ScanDataResults(
    @SerializedName("data") var data: ArrayList<ScanData> = arrayListOf()
)