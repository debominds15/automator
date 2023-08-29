package com.example.autoreportgenerator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.model.ScanRequest
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.repo.RegistrationRepo
import kotlinx.coroutines.*

class HomeViewModel(private val homeRepository: HomeRepo) : ViewModel() {

    val homeState = MutableLiveData<Boolean>()
    var token = ""

    var job: Job? = null

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }

    fun postScan(model: ScanRequest, token: String) {
        this.token = token

        job = CoroutineScope(Dispatchers.IO).launch {
            val response: RegisterResponse = homeRepository.postScan(getHeaderMap(), model )
            withContext(Dispatchers.Main) {
                if (response.code == 201)
                        homeState.postValue(true)
                    else
                        homeState.postValue(false)
                }
            }
    }

    fun download(token: String) {
        this.token = token

        job = CoroutineScope(Dispatchers.IO).launch {
            val responseValidate = homeRepository.genReport(getHeaderMap())
            withContext(Dispatchers.Main) {
                /*if (responseValidate.code == 201) {

                } else {
                    homeState.postValue(false)
                }*/
            }


        }
    }

}