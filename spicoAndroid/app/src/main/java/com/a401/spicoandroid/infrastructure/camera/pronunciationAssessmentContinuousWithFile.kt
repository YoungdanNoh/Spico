package com.a401.spicoandroid.infrastructure.camera

import android.content.Context
import com.microsoft.cognitiveservices.speech.*
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import android.media.MediaPlayer
import android.util.Log
import com.a401.spicoandroid.BuildConfig.AZURE_KEY
import com.a401.spicoandroid.BuildConfig.AZURE_REGION
import androidx.core.net.toUri
import java.io.File

fun pronunciationAssessmentContinuousWithFile(
    context: Context,
    audioFile: File,
    script: String): Map<String, Any> {
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
    pronConfig.enableProsodyAssessment()

    val recognizer = SpeechRecognizer(speechConfig, "ko-KR", audioConfig)

    pronConfig.applyTo(recognizer)
    Log.d("AzurePronunciation", "✅ 발음 평가 적용 완료")


    var done = false
    var fullText = ""

    val pauseIssues = mutableListOf<Pair<Long, Long>>()
    val speedIssues = mutableListOf<Pair<Long, Long>>() // 말이 너무 빠르거나 느린 구간
    val volumeIssues = mutableListOf<Pair<Long, Long>>() // 목소리가 너무 작거나 큰 구간
    val pronunciationIssues = mutableListOf<Pair<Long, Long>>() // 발음 정확도가 낮은 구간

    var prevOffset: Long? = null

    val recognizedWords = mutableListOf<String>()
    var accuracyTotal = 0.0
    var completenessTotal = 0.0
    var fluencyTotal = 0.0
    var pronunciationTotal = 0.0
    var wordCount = 0

    recognizer.sessionStopped.addEventListener { _, _ ->
        done = true
    }

    recognizer.recognized.addEventListener { _, e ->
        fullText += e.result.text + " "
        Log.d("AzurePronunciation", "🎤 인식됨: ${e.result.text}")

        val pronResult = PronunciationAssessmentResult.fromResult(e.result)
        Log.d("AzurePronunciation", "🔹 정확도=${pronResult.accuracyScore}, 유창성=${pronResult.fluencyScore}, 발음=${pronResult.pronunciationScore}")

        accuracyTotal += pronResult.accuracyScore
        completenessTotal += pronResult.completenessScore
        fluencyTotal += pronResult.fluencyScore
        pronunciationTotal += pronResult.pronunciationScore
        wordCount++

        for (word in pronResult.words) {
            recognizedWords.add(word.word)
            val offset = word.offset
            val duration = word.duration
            val endOffset = offset + duration

            Log.d("AzurePronunciation", "🔹 단어 인식: ${word.word} (Offset=$offset, Duration=$duration)")

            if (word.accuracyScore < 60) {
                pronunciationIssues.add(offset to (offset + duration))
                Log.w("AzurePronunciation", "⚠️ 발음 정확도가 낮음: ${word.word} (${word.accuracyScore})")
            }

            if (prevOffset != null) {
                val pause = offset - (prevOffset!! + duration)
                if (pause > 3000000) {
                    pauseIssues.add(prevOffset!! to offset)
                }
            }

            prevOffset = offset
        }
    }


    try {
        recognizer.startContinuousRecognitionAsync().get()
        Log.d("AzurePronunciation", "🚀 STT 시작됨")
    } catch (e: Exception) {
        Log.e("AzurePronunciation", "❌ 발음 평가 시작 중 오류 발생: ${e.message}")
    }

    Log.d("AzurePronunciation", "🚀 발음 평가 진행 중...")


    val mediaPlayer = MediaPlayer().apply {
        setDataSource(context, audioFile.absolutePath.toUri())
        prepare() // 🔹 준비 후 실행
    }

    if (!mediaPlayer.isPlaying) {
        Log.w("AzurePronunciation", "⚠️ MediaPlayer가 재생되지 않은 상태에서 AudioSessionId를 호출하려 함!")
    }


    val duration = mediaPlayer.duration.toLong() // ms 단위

    while (!done && duration > 0) {
        Thread.sleep(1000)
    }

    recognizer.stopContinuousRecognitionAsync().get()
    Log.d("AzurePronunciation", "✅ 발음 평가 완료")

    val volumeLevel = try {
        mediaPlayer.audioSessionId.toDouble() // 🔹 예외 발생 가능
    } catch (e: IllegalStateException) {
        Log.e("AzurePronunciation", "❌ MediaPlayer 오류 발생: ${e.message}")
        50.0 // 기본값 반환
    }
    mediaPlayer.release()

    var totalPauseTime = 0L
    pauseIssues.forEach { (start, end) ->
        totalPauseTime += end - start
    }
    val penalty = totalPauseTime / 100L // 예: 1초당 10점 감점
    val pauseScore = (100 - penalty).coerceIn(60, 100)


    val volumeScore = when {
        volumeLevel in 30.0..70.0 -> 100
        volumeLevel < 30.0 -> 80
        else -> 70
    }

    val speechRate = recognizedWords.size / (duration / 1000.0)
    val speedScore = when {
        speechRate in 1.5..3.0 -> 100
        speechRate < 1.5 -> 80.also { speedIssues.add(0L to duration) }
        else -> 70.also { speedIssues.add(0L to duration) }
    }

    val accuracyAvg = accuracyTotal / wordCount
    val completenessAvg = completenessTotal / wordCount
    val fluencyAvg = fluencyTotal / wordCount
    val pronunciationAvg = pronunciationTotal / wordCount

    fun formatTime(tick: Long): String {
        val ms = tick / 10_000 // 100ns -> ms
        val minutes = ms / 60000
        val seconds = (ms % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    val issueDetails = mapOf(
        "pauseIssues" to pauseIssues.map { formatTime(it.first) to formatTime(it.second) },
        "speedIssues" to speedIssues.map { formatTime(it.first) to formatTime(it.second) },
        "volumeIssues" to volumeIssues.map { formatTime(it.first) to formatTime(it.second) },
        "pronunciationIssues" to pronunciationIssues.map { formatTime(it.first) to formatTime(it.second) }
    )

    Log.d("AzurePronunciation", "📝 최종 분석 결과: $issueDetails")


    return mapOf(
        "transcribedText" to fullText.trim(),
        "accuracyScore" to accuracyAvg,
        "completenessScore" to completenessAvg,
        "fluencyScore" to fluencyAvg,
        "pronunciationScore" to pronunciationAvg,
        "pauseScore" to pauseScore,
        "volumeScore" to volumeScore,
        "speedScore" to speedScore,
        "issueDetails" to issueDetails
    )
}