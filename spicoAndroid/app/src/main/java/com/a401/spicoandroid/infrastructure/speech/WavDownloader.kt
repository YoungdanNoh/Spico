package com.a401.spicoandroid.infrastructure.speech

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object WavDownloader {

    suspend fun downloadWavFile(presignedUrl: String): File = withContext(Dispatchers.IO) {
        val url = URL(presignedUrl)
        val connection = url.openConnection()
        val inputStream = connection.getInputStream()
        val tempFile = File.createTempFile("azure_input", ".wav")

        Log.d("AzurePronunciation", ".wav 다운로드 시작")

        tempFile.outputStream().use { outputStream ->

            inputStream.copyTo(outputStream)
            Log.d("AzurePronunciation", ".wav 다운로드 완료")
        }

        tempFile
    }
}