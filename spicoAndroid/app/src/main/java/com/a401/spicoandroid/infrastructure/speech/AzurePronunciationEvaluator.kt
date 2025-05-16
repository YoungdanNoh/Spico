package com.a401.spicoandroid.infrastructure.speech

import android.content.Context
import android.util.Log
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import com.microsoft.cognitiveservices.speech.PropertyId
import com.microsoft.cognitiveservices.speech.ResultReason
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognizer
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AzurePronunciationEvaluator(
    private val subscriptionKey: String,
    private val region: String
) {

    suspend fun evaluatePronunciation(
        wavFile: File,
        referenceText: String, // 기준이 되는 문장
        context: Context
    ): String = withContext(Dispatchers.IO) {
        val speechConfig = SpeechConfig.fromSubscription(subscriptionKey, region)
        speechConfig.speechRecognitionLanguage = "ko-KR"

        val audioConfig = AudioConfig.fromWavFileInput(wavFile.absolutePath)

        val assessmentConfig = PronunciationAssessmentConfig(
            referenceText,
            PronunciationAssessmentGradingSystem.HundredMark,
            PronunciationAssessmentGranularity.Phoneme,
            true
        )

        val recognizer = SpeechRecognizer(speechConfig, audioConfig)
        assessmentConfig.applyTo(recognizer)

        val result = recognizer.recognizeOnceAsync().get()

        recognizer.close()
        audioConfig.close()
        speechConfig.close()

        if (result.reason == ResultReason.RecognizedSpeech) {
            val json = result.properties.getProperty(PropertyId.SpeechServiceResponse_JsonResult)
            Log.d("AzurePronunciation", "전체 결과 JSON: $json")
            json
        } else {
            throw Exception("AzurePronunciation: ${result.reason}")
        }
    }
}