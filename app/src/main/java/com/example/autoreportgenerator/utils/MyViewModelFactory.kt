package com.example.autoreportgenerator.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.viewmodel.RegistrationViewModel

class MyViewModelFactory constructor(private val repository: RegistrationRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            RegistrationViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}