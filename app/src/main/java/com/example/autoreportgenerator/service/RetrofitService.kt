package com.example.autoreportgenerator.service

import com.example.autoreportgenerator.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @POST("api/auth/register")
    suspend fun submitRegistration(@Body registermodel: RegistrationUser) : Response<RegisterResponse>

    @GET("api/auth/verify/{token}")
    suspend fun verifyRegistration(@Path("token") token: String) : Response<RegisterResponse>

    @Headers("Accept: application/json")
    @POST("api/auth/login")
    suspend fun submitLogin(@Body loginModel: LoginUser): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("api/auth")
    suspend fun verifyLogin(@HeaderMap headers: Map<String, String>): Response<LoginValidateResponse>

    @POST("api/auth/scan")
    suspend fun postScan(@HeaderMap headers: Map<String, String>, @Body model: ScanRequest) : Response<RegisterResponse>

    @GET("api/report")
    suspend fun fetchReport(@HeaderMap headers: Map<String, String>) : Response<Void>



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