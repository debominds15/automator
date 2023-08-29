package com.example.autoreportgenerator.service

import com.example.autoreportgenerator.model.RegisterResponse
import com.example.autoreportgenerator.model.RegistrationUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    @POST("api/auth/register")
    suspend fun submitRegistration(@Body registermodel: RegistrationUser) : Response<RegisterResponse>

    @GET("api/auth/verify/{token}")
    suspend fun verifyRegistration(@Path("token") token: String) : Response<RegisterResponse>


    companion object {
        var retrofitService: RetrofitService? = null
        private var interceptor: HttpLoggingInterceptor =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://aqueous-journey-81353-504e20ebc0cc.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}