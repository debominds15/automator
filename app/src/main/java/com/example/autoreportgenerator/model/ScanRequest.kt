package com.example.autoreportgenerator.model

import com.google.gson.annotations.SerializedName

data class ScanRequest(

    @SerializedName("userId") var userId: String? = null,
    @SerializedName("patientId") var patientId: String? = null,
    @SerializedName("patientName") var patientName: String? = null,
    @SerializedName("patientAge") var patientAge: String? = null,
    @SerializedName("height") var height: String? = null,
    @SerializedName("weight") var weight: String? = null,
    @SerializedName("bmi") var bmi: String? = null,
    @SerializedName("scannedByDoctor") var scannedBy: String? = null,
    @SerializedName("isFirstVisit") var isFirstVisit: Boolean? = null,
    @SerializedName("scanned_on") var scannedOn: String? = null,
    @SerializedName("fhr") var fhr: String? = null,
    @SerializedName("ga") var ga: String? = null,
    @SerializedName("mvp") var mvp: String? = null,
    @SerializedName("placentaLocation") var placentaLocation: String? = null,
    @SerializedName("pdfUrl") var pdfUrl: String? = null,
    @SerializedName("summary") var summary: String? = null,
    @SerializedName("publicId") var publicId: String? = null

)