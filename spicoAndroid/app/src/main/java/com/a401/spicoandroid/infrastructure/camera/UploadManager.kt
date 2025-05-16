package com.a401.spicoandroid.infrastructure.camera

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadManager @Inject constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient
) {
    suspend fun uploadVideoToPresignedUrl(presignedUrl: String, file: File): Boolean {
        return try {
            val requestBody = file.asRequestBody("video/mp4".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(presignedUrl)
                .put(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}