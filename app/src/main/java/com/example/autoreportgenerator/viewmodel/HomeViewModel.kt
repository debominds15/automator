package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.repo.HomeRepo
import kotlinx.coroutines.*

class HomeViewModel(private val homeRepository: HomeRepo) : ViewModel() {

    val homeState = MutableLiveData<Boolean>()
    val homeScanData = MutableLiveData<ArrayList<ScanData>>(arrayListOf())
    val homePatientsData = MutableLiveData<ArrayList<PatientData>>(arrayListOf())
    var token = ""

    var job: Job? = null

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }


    fun getAllScanData(token: String) {
        this.token = token
        job = CoroutineScope(Dispatchers.IO).launch {
            val response: ScanResponse = homeRepository.getAllScanData(getHeaderMap())
            withContext(Dispatchers.Main) {
                if (response.code == 200)
                    homeScanData.postValue(response.results?.data ?: arrayListOf())
                else
                    homeScanData.postValue(arrayListOf())
            }
        }
    }

    fun getAllListOfPatients(token: String) {
        this.token = token
        job = CoroutineScope(Dispatchers.IO).launch {
            val response: PatientResponse = homeRepository.fetchAllPatients(getHeaderMap())
            withContext(Dispatchers.Main) {
                if (response.code == 200)
                    homePatientsData.postValue(response.results?.data ?: arrayListOf())
                else
                    homePatientsData.postValue(arrayListOf())
            }
        }
    }

    fun generateReportUploadScanData(scanData: ScanRequest, token: String) {
        this.token = token

        job = CoroutineScope(Dispatchers.IO).launch {
            val responseReport = homeRepository.genReport(getHeaderMap(), scanData)
            if (responseReport.code == 201) {
                scanData.pdfUrl = responseReport.loginresults?.file?.pdfUrl
                val response: RegisterResponse = homeRepository.postScan(getHeaderMap(), scanData)
                withContext(Dispatchers.Main) {
                    if (response.code == 201)
                        homeState.postValue(true)
                    else
                        homeState.postValue(false)
                }
                /*val responseDownloadReport = homeRepository.downloadReport(responseReport.message?: "", fileUrl)
                homeDownloadState.postValue(responseDownloadReport)*/
            }
        }
    }

}