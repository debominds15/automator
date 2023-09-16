package com.example.autoreportgenerator.utils

import android.content.Context
import android.util.Log
import com.example.autoreportgenerator.model.chatgpt.PatientInputParams
import org.json.JSONObject
import java.io.File


object Util {

    fun readAndParseJSON(path: String, context: Context): PatientInputParams {
        // Now you can read and parse the JSON file from external storage
        val filePath = context.getExternalFilesDir(null)?.absolutePath + "/Temp/result.json"
        val file = File(filePath)
        Log.d("Util", "file path is ${file.absolutePath}")
        if (file.exists()) {
            try {
                val jsonString = file.readText()
                //val jsonArray = JSONArray(jsonString)
                val jsonObject = JSONObject(jsonString).getJSONObject("ExamResult")

                return PatientInputParams(
                    jsonObject["GestationalAge"] as Double? ?: 0.0,
                    jsonObject["DVPinCM"] as Double? ?: 0.0,
                    jsonObject["CAViability"] as String? ?: "",
                    jsonObject["PlacentaLocation"] as String? ?: "",
                    jsonObject["FetalPresentation"] as String? ?: "",
                    jsonObject["FetalHeartRate"] as String? ?: "",
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Handle the case where the file doesn't exist
        }
        return PatientInputParams()
    }

}