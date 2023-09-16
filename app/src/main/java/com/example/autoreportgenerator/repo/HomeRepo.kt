package com.example.autoreportgenerator.repo

import com.example.autoreportgenerator.model.*
import com.example.autoreportgenerator.model.chatgpt.ChatGptRequest
import com.example.autoreportgenerator.model.chatgpt.GeneratedSummary
import com.example.autoreportgenerator.service.Resource
import com.example.autoreportgenerator.service.RetrofitChatService
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.NetworkUtil
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class HomeRepo constructor(
    private val retrofitService: RetrofitService,
    private val chatService: RetrofitChatService
) {

    suspend fun postScan(headers: Map<String, String>, model: ScanRequest): RegisterResponse =
        retrofitService.postScan(headers, model).body()!!

    suspend fun updateScan(
        headers: Map<String, String>,
        scanId: String,
        model: ScanRequest
    ): RegisterResponse =
        retrofitService.updateScan(headers, scanId, model).body()!!

    suspend fun getAllScanData(headers: Map<String, String>): ScanResponse =
        retrofitService.fetchAllScanDataForDoctors(headers).body()!!

    suspend fun getAllReportData(headers: Map<String, String>): ScanResponse =
        retrofitService.fetchAllReportDataForPatients(headers).body()!!

    suspend fun fetchAllPatients(headers: Map<String, String>): PatientResponse =
        retrofitService.getAllPatients(headers).body()!!

    suspend fun genReport(headers: Map<String, String>, model: ScanRequest): FileReportResponse =
        retrofitService.uploadReport(headers, model).body()!!

    suspend fun deleteReport(
        headers: Map<String, String>,
        model: DeleteReportRequest
    ): FileReportResponse =
        retrofitService.deleteOldReport(headers, model).body()!!

    suspend fun submitQuery(requestModel: ChatGptRequest): Resource<GeneratedSummary> {
        val response = chatService.submitSummary(requestModel)
        val result = response.body()
        return try {

            //Handling api success responses (201, 200)
            if (response.isSuccessful && result != null) {

                Resource.Success(result)

            } else {
                //Handling api error response (501, 404)
                Resource.ErrorRes(NetworkUtil.getErrorResponse(response.errorBody()!!.string()))

            }
        } catch (e: Exception) {
            //This is for handling errors where we sometimes misconfigured retrofit instance
            Resource.Error(e.message ?: "An error occured")
        }
    }

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