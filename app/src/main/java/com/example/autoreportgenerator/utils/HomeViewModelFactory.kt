package com.example.autoreportgenerator.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.repo.RegistrationRepo
import com.example.autoreportgenerator.viewmodel.HomeViewModel
import com.example.autoreportgenerator.viewmodel.LoginViewModel
import com.example.autoreportgenerator.viewmodel.RegistrationViewModel

class HomeViewModelFactory constructor(private val repository: HomeRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}