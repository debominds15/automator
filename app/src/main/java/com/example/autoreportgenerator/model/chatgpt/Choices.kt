package com.example.autoreportgenerator.model.chatgpt

import com.google.gson.annotations.SerializedName

data class Choices(

    @SerializedName("finish_reason") var finishReason: String? = null,
    @SerializedName("index") var index: Int? = null,
    @SerializedName("message") var message: Messages? = Messages()

)