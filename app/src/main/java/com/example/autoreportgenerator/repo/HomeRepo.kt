package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.service.RetrofitService
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class HomeRepo constructor(private val retrofitService: RetrofitService) {

    suspend fun postScan(headers: Map<String, String>, model: ScanRequest): RegisterResponse =
        retrofitService.postScan(headers, model).body()!!

    suspend fun getAllScanData(headers: Map<String, String>): ScanResponse =
        retrofitService.fetchAllScanData(headers).body()!!

    suspend fun fetchAllPatients(headers: Map<String, String>): PatientResponse =
        retrofitService.getAllPatients(headers).body()!!

    suspend fun genReport(headers: Map<String, String>, model: ScanRequest): FileReportResponse =
        retrofitService.uploadReport(headers, model).body()!!

    suspend fun downloadReport(url: String, fileUrl: String): Boolean {
        val call = retrofitService.downloadReport(url)
        var isDownloaded = false
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    writeResponseBodyToDisk(response.body()!!, fileUrl)
                    isDownloaded = true
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isDownloaded = false
            }
        })

        return isDownloaded
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, fileUrl: String): Boolean {
        return try {
            val futureStudioIconFile =
                File(fileUrl)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

}