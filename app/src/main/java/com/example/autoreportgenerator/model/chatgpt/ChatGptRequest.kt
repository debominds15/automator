package com.example.autoreportgenerator.model.chatgpt

import com.google.gson.annotations.SerializedName

data class ChatGptRequest(
    @SerializedName("model") var model: String? = null,
    @SerializedName("messages") var messages: ArrayList<Messages> = arrayListOf(),
    @SerializedName("temperature") var temperature: Double? = null
)