package com.a401.spicoandroid.infrastructure.camera

import com.microsoft.cognitiveservices.speech.*
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import android.media.MediaPlayer
import com.a401.spicoandroid.BuildConfig.AZURE_KEY
import com.a401.spicoandroid.BuildConfig.AZURE_REGION
import androidx.core.net.toUri
import java.io.File

suspend fun pronunciationAssessmentContinuousWithFile(audioFile: File, script: String) {
    // Azure Speech 서비스 설정
    val subscriptionKey = AZURE_KEY
    val serviceRegion = AZURE_REGION

    val speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion)
    val audioConfig = AudioConfig.fromWavFileInput(audioFile.absolutePath)

    // 평가할 텍스트
    val referenceText = script

    // 발음 평가 구성
    val pronConfig = PronunciationAssessmentConfig(referenceText,
        PronunciationAssessmentGradingSystem.HundredMark,
        PronunciationAssessmentGranularity.Phoneme, true)

    val recognizer = SpeechRecognizer(speechConfig, "ko-KR", audioConfig)

    pronConfig.applyTo(recognizer)

    var done = false
    recognizer.sessionStopped.addEventListener { _, _ ->
        println("세션 종료됨")
        done = true
    }

    recognizer.recognized.addEventListener { _, e ->
        println("인식됨: ${e.result.text}")
        val pronResult = PronunciationAssessmentResult.fromResult(e.result)
        println("정확도: ${pronResult.accuracyScore}, 유창성: ${pronResult.fluencyScore}, 발음 점수: ${pronResult.pronunciationScore}")
    }

    recognizer.startContinuousRecognitionAsync().get()

    // 📢 **파일 길이에 맞게 인식 유지**
    val mediaPlayer = MediaPlayer.create(null, audioFile.absolutePath.toUri())
    val duration = mediaPlayer.duration.toLong() // ms 단위
    mediaPlayer.release()

    while (!done && duration > 0) {
        Thread.sleep(1000) // 지속적으로 이벤트 확인
    }

    recognizer.stopContinuousRecognitionAsync().get()
}