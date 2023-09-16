package com.example.autoreportgenerator.model.chatgpt

data class PatientInputParams(
    val gestationalAge: Double? = 0.0,
    val dVPinCM: Double? = 0.0,
    val CAViability: String? = "-",
    val PlacentaLocation: String? = "-",
    val FetalPresentation: String? = "-",
    val FetalHeartRate: String? = "-"
)