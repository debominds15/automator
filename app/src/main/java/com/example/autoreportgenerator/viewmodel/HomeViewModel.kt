package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autoreportgenerator.data.SingleLiveEvent
import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.model.chatgpt.ChatGptRequest
import com.example.autoreportgenerator.model.chatgpt.Messages
import com.example.autoreportgenerator.model.chatgpt.PatientInputParams
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.service.Resource
import com.example.autoreportgenerator.service.SummaryService
import kotlinx.coroutines.*

class HomeViewModel(private val homeRepository: HomeRepo) : ViewModel() {

    val homeState = SingleLiveEvent<Boolean>()
    val homeUploadFile = MutableLiveData<Boolean>()
    val homeDeleteOldFile = MutableLiveData<Boolean>()
    val homeScanData = MutableLiveData<ArrayList<ScanData>>(arrayListOf())
    val homeReportData = MutableLiveData<ArrayList<ScanData>>(arrayListOf())
    val homePatientsData = MutableLiveData<ArrayList<PatientData>>(arrayListOf())
    val homeEvent = MutableLiveData<HomeEvent<Any>>()
    var token = ""

    sealed class HomeEvent<out T : Any> {

        class Success<out T : Any>(val homeResponse: T) : HomeEvent<T>()
        class ErrorResponseResult(val errorResponse: ErrorResponse) : HomeEvent<Nothing>()
        class Failure(val message: String?) : HomeEvent<Nothing>()
        object Loading : HomeEvent<Nothing>()
        object Empty : HomeEvent<Nothing>()

    }

    var job: Job? = null

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }


    fun getAllScanData(token: String) { //for doctors
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

    fun getAllReportData(token: String) { //for patients
        this.token = token
        job = CoroutineScope(Dispatchers.IO).launch {
            val response: ScanResponse = homeRepository.getAllReportData(getHeaderMap())
            withContext(Dispatchers.Main) {
                if (response.code == 200)
                    homeReportData.postValue(response.results?.data ?: arrayListOf())
                else
                    homeReportData.postValue(arrayListOf())
            }
        }
    }

    fun getReportNormalcyData(jsonData: PatientInputParams): String {
        return SummaryService.checkReportNormal(jsonData)
    }

    fun sendDataToGenerateSummary(jsonData: PatientInputParams) {
        val query = SummaryService.getQueryToGenerateSummary(jsonData)
        val requestModel = ChatGptRequest(
            model = "gpt-3.5-turbo",
            messages = arrayListOf(Messages(role = "user", content = query)),
            temperature = 0.7
        )

        homeEvent.value = HomeEvent.Loading
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = homeRepository.submitQuery(requestModel)
            withContext(Dispatchers.Main) {
                when (response) {

                    is Resource.Success -> {
                        homeEvent.value =
                            HomeEvent.Success(response.data!!)
                    }

                    is Resource.ErrorRes -> {
                        homeEvent.value = HomeEvent.ErrorResponseResult(
                            response.errorResponse
                                ?: ErrorResponse(message = "Something went wrong, please try again!")
                        )
                    }

                    else -> {

                    }
                }
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

    fun generateReportUploadScanData(scanData: ScanRequest, token: String) { // for doctor
        this.token = token

        job = CoroutineScope(Dispatchers.IO).launch {
            val responseReport = homeRepository.genReport(getHeaderMap(), scanData)
            if (responseReport.code == 201) {
                homeUploadFile.postValue(true)
                scanData.pdfUrl = responseReport.loginresults?.file?.pdfUrl
                scanData.publicId = responseReport.loginresults?.file?.pdfId
                val response: RegisterResponse = homeRepository.postScan(getHeaderMap(), scanData)
                withContext(Dispatchers.Main) {
                    if (response.code == 201)
                        homeState.postValue(true)
                    else
                        homeState.postValue(false)
                }
            } else {
                homeUploadFile.postValue(false)
            }
        }
    }

    fun updateReportUploadScanData(scanData: ScanRequest, id: String, token: String) { // for doctor
        this.token = token

        job = CoroutineScope(Dispatchers.IO).launch {
            val deleteRequest = DeleteReportRequest(scanData.publicId ?: "")
            val responseDeleteReport = homeRepository.deleteReport(getHeaderMap(), deleteRequest)
            if (responseDeleteReport.code == 201) {
                homeDeleteOldFile.postValue(true)
                val responseReport = homeRepository.genReport(getHeaderMap(), scanData)
                if (responseReport.code == 201) {
                    homeUploadFile.postValue(true)
                    scanData.pdfUrl = responseReport.loginresults?.file?.pdfUrl
                    scanData.publicId = responseReport.loginresults?.file?.pdfId
                    val response: RegisterResponse =
                        homeRepository.updateScan(getHeaderMap(), id, scanData)
                    withContext(Dispatchers.Main) {
                        if (response.code == 201)
                            homeState.postValue(true)
                        else
                            homeState.postValue(false)
                    }
                } else {
                    homeDeleteOldFile.postValue(false)
                }
            }
        }
    }

}