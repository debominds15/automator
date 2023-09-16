package com.example.autoreportgenerator.model.chatgpt

import com.google.gson.annotations.SerializedName

data class Messages(

    @SerializedName("content") var content: String? = null,
    @SerializedName("role") var role: String? = null

)