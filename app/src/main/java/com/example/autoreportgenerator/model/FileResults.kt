package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class FileResults(
    @SerializedName("file") var file: File? = File()
)