package com.example.autoreportgenerator.model.chatgpt

import com.google.gson.annotations.SerializedName


data class Usage(

    @SerializedName("completion_tokens") var completionTokens: Int? = null,
    @SerializedName("prompt_tokens") var promptTokens: Int? = null,
    @SerializedName("total_tokens") var totalTokens: Int? = null

)