package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("user") var user: User? = User(),
    @SerializedName("verification") var verification: Verification? = Verification()

)