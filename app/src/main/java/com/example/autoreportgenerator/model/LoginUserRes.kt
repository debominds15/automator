package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName


data class LoginUserRes(
    @SerializedName("_id"              ) var Id               : String?  = null,
    @SerializedName("name"             ) var name             : String?  = null,
    @SerializedName("email"            ) var email            : String?  = null,
    @SerializedName("isRoleTypeDoctor" ) var isRoleTypeDoctor : Boolean? = null,
    @SerializedName("verified"         ) var verified         : Boolean? = null,
    @SerializedName("createdAt"        ) var createdAt        : String?  = null,
    @SerializedName("__v"              ) var _v               : Int?     = null,
    @SerializedName("verifiedAt"       ) var verifiedAt       : String?  = null

)