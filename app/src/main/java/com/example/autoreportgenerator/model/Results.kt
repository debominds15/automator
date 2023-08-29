package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("user") var user: User? = User(),
    @SerializedName("verification") var verification: Verification? = Verification(),

    @SerializedName("id") var id: String? = null,
    @SerializedName("fhr") var fhr: String? = null,
    @SerializedName("ga") var ga: String? = null,
    @SerializedName("mvp") var mvp: String? = null,
    @SerializedName("placentalocation") var placentalocation: String? = null,
    @SerializedName("summary") var summary: String? = null

)