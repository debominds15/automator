package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("isRoleTypeDoctor")
    var isRoleTypeDoctor: Boolean? = null,
    @SerializedName("verified")
    var verified: Boolean? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null

)