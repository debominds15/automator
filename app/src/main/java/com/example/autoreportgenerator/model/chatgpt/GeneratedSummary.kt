package com.example.autoreportgenerator.model.chatgpt

import com.google.gson.annotations.SerializedName

data class GeneratedSummary(
    @SerializedName("choices") var choices: ArrayList<Choices> = arrayListOf(),
    @SerializedName("created") var created: Int? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("model") var model: String? = null,
    @SerializedName("object") var objectChat: String? = null,
    @SerializedName("usage") var usage: Usage? = Usage()
)