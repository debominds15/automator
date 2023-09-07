package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class PatientDataResults (
    @SerializedName("data" ) var data : ArrayList<PatientData> = arrayListOf()
)