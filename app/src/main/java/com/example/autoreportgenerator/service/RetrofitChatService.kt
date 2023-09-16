package com.example.autoreportgenerator.service

import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.model.chatgpt.ChatGptRequest
import com.example.autoreportgenerator.model.chatgpt.GeneratedSummary
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitChatService {

    @POST("v1/chat/completions")
    suspend fun submitSummary(@Body requestModel: ChatGptRequest): Response<GeneratedSummary>


    companion object {
        var retrofitService: RetrofitChatService? = null
        private var interceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private const val BASE_URL = "https://api.openai.com/"

        private val token = "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"
        private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request =
                chain.request().newBuilder().addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer $token").build()
            chain.proceed(newRequest)
        }
            .addInterceptor(interceptor)
            .build()


        fun getInstance(): RetrofitChatService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitChatService::class.java)
            }
            return retrofitService!!
        }

    }
}