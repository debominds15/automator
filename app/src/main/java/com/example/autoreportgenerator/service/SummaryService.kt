package com.example.autoreportgenerator.service

import com.example.autoreportgenerator.model.chatgpt.PatientInputParams
import java.util.*


object SummaryService {
    val inputJson = mapOf(
        "GestationalAge" to 247.4029541015625,
        "DVP" to "6cm",
        "FHR" to "160 bpm",
        "Placenta" to "normal",
        "FetalPresentation" to "Cephalic"
    )

    fun daysToWeeksAndDays(days: Double): Pair<Int, Int> {
        val weeks = (days / 7).toInt()
        val remainingDays = (days % 7).toInt()
        return Pair(weeks, remainingDays)
    }

    fun compareToRange(value: Double, rangeStr: String): Boolean {
        if ('-' in rangeStr) {
            val (minValue, maxValue) = rangeStr.split('-').map {
                it.trim()
            }
            if (minValue.isNotEmpty() && maxValue.isNotEmpty()) {
                return value >= minValue.toDouble() && value <= maxValue.toDouble()
            }
        }
        return false
    }

    fun checkCondition(value: Any, conditionStr: String, normalCondition: Boolean = true): Boolean {
        return if (conditionStr.lowercase(Locale.getDefault()) == "normal") normalCondition else !normalCondition
    }

    fun getQueryToGenerateSummary(model: PatientInputParams): String {
        val (gestationalWeeks, gestationalDays) = daysToWeeksAndDays(model.gestationalAge ?: 0.0)

        return """
        Write a maximum five line ultrasound report on:
        A fetus of GA $gestationalWeeks Weeks and $gestationalDays Days,
        FHR ${model.FetalHeartRate ?: "-"},
        Cephalic representation ${model.FetalPresentation ?: "normal"},
        Placenta ${model.PlacentaLocation ?: "-"},
        Multiple Gestation  ${model.PlacentaLocation ?: "non-singleton"},
        Deepest vertical Pocket ${model.dVPinCM}.
         """.trimIndent()
    }

    fun checkReportNormal(model: PatientInputParams): String {

        val gestationalAgeDays = model.gestationalAge ?: 0.0
        val (gestationalWeeks, gestationalDays) = daysToWeeksAndDays(gestationalAgeDays)
        val dvpValue = model.dVPinCM ?: 0.0
        val fhrValue = model.FetalHeartRate ?: "0 bpm"
        val placentaCondition = model.PlacentaLocation ?: "normal"
        val fetalPresentationCondition = model.FetalPresentation ?: "normal"


        val fhrIsNormal = compareToRange(fhrValue.toDouble(), "120-180 bpm")
        val dvpIsNormal = compareToRange(dvpValue, "2-8")
        val placentaIsNormal = checkCondition(0, placentaCondition)
        val fetalPresentationIsNormal =
            checkCondition("Cephalic", fetalPresentationCondition, false)

        val condition =
            if (!(fhrIsNormal && dvpIsNormal && placentaIsNormal && fetalPresentationIsNormal)) {
                "Abnormal"
            } else {
                "Normal"
            }

        return condition
    }
}



