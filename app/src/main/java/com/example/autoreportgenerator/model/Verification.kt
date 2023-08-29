package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class Verification (

    @SerializedName("token"     ) var token     : String? = null,
    @SerializedName("userId"    ) var userId    : String? = null,
    @SerializedName("type"      ) var type      : String? = null,
    @SerializedName("_id"       ) var Id        : String? = null,
    @SerializedName("createdAt" ) var createdAt : String? = null,
    @SerializedName("__v"       ) var _v        : Int?    = null

)