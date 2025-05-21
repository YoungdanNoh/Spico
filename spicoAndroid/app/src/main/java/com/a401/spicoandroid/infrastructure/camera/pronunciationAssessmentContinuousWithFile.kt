package com.a401.spicoandroid.infrastructure.camera

import android.content.Context
import com.microsoft.cognitiveservices.speech.*
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.util.Log
import com.a401.spicoandroid.BuildConfig.AZURE_KEY
import com.a401.spicoandroid.BuildConfig.AZURE_REGION
import androidx.core.net.toUri
import com.a401.spicoandroid.domain.finalmode.model.IssueDetails
import com.a401.spicoandroid.domain.finalmode.model.TimeRange
import java.io.File
import java.io.RandomAccessFile
import java.time.Duration
import kotlin.math.log10
import kotlin.math.sqrt

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
    var assessmentCount = 0

    recognizer.sessionStopped.addEventListener { _, _ ->
        done = true
    }

    recognizer.recognized.addEventListener { _, e ->
        fullText += e.result.text + " "
        assessmentCount += 1
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
                if (pause > 15000000) { // 1.5초 (15,000,000 = 1.5s in 100ns 단위)
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

    // 볼륨 레벨 측정 (WAV 파일에서 직접 측정)
    val volumeLevel = measureVolumeFromWavFile(audioFile)
    Log.d("AzurePronunciation", "🔊 측정된 볼륨 레벨: ${volumeLevel}dB")

    // 실시간 볼륨 모니터링 (선택사항)
    var visualizer: Visualizer? = null
    val volumeLevels = mutableListOf<Double>()

    try {
        if (mediaPlayer.audioSessionId != 0) {
            visualizer = Visualizer(mediaPlayer.audioSessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]

                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        visualizer: Visualizer?,
                        waveform: ByteArray?,
                        samplingRate: Int
                    ) {
                        waveform?.let {
                            val rms = calculateRMS(it)
                            val db = 20 * log10(rms / 32768.0) // 16비트 오디오 기준
                            volumeLevels.add(db)
                        }
                    }

                    override fun onFftDataCapture(
                        visualizer: Visualizer?,
                        fft: ByteArray?,
                        samplingRate: Int
                    ) {}
                }, Visualizer.getMaxCaptureRate() / 2, true, false)

                enabled = true
            }

            mediaPlayer.start()
        }
    } catch (e: Exception) {
        Log.w("AzurePronunciation", "⚠️ Visualizer 초기화 실패: ${e.message}")
    }

    // 리소스 정리
    visualizer?.enabled = false
    visualizer?.release()
    mediaPlayer.release()

    // 볼륨 분석
    val avgVolumeLevel = if (volumeLevels.isNotEmpty()) {
        volumeLevels.average()
    } else {
        volumeLevel
    }

    // 볼륨이 너무 낮은 구간 찾기
    val lowVolumeThreshold = -30.0 // dB
    volumeLevels.forEachIndexed { index, db ->
        if (db < lowVolumeThreshold) {
            val timeMs = (index * 1000L) / (volumeLevels.size.toDouble() / (duration / 1000.0)).toLong()
            volumeIssues.add(timeMs to (timeMs + 1000))
        }
    }


// 볼륨 점수 계산시 상대적 기준 적용
// 노이즈 플로어를 측정하고 그 위에 얼마나 있는지를 기준으로 평가
    val volumeScore = when {
        avgVolumeLevel >= -25.0 -> 100  // 적정 볼륨
        avgVolumeLevel >= -35.0 -> 90   // 약간 낮음
        avgVolumeLevel >= -45.0 -> 80   // 낮음
        avgVolumeLevel >= -55.0 -> 70   // 매우 낮음
        else -> 60  // 거의 들리지 않음
    }

    val wordCountInScript = script.split("\\s+".toRegex()).size
    val expectedDuration = wordCountInScript * 0.5 // 평균 단어당 0.5초로 예상
    val durationInSeconds = duration / 1000.0
    val speedRatio = durationInSeconds / expectedDuration

    val speedScore = when {
        speedRatio in 0.8..1.3 -> 100 // 예상 시간의 80%~130% 사이는 적정 속도
        speedRatio < 0.8 -> 90.also { speedIssues.add(0L to duration) } // 예상보다 빠름
        speedRatio < 0.6 -> 80.also { speedIssues.add(0L to duration) } // 너무 빠름
        speedRatio > 1.3 -> 85.also { speedIssues.add(0L to duration) } // 예상보다 느림
        speedRatio > 1.6 -> 70.also { speedIssues.add(0L to duration) } // 너무 느림
        else -> 60.also { speedIssues.add(0L to duration) }
    }

    val accuracyAvg = accuracyTotal / assessmentCount
    val completenessAvg = completenessTotal / assessmentCount
    val fluencyAvg = fluencyTotal / assessmentCount
    val pronunciationAvg = pronunciationTotal / assessmentCount
    Log.d("AzurePronunciation", "accuracyAvg: $accuracyAvg, completnessAvg: $completenessAvg")


    var totalPauseTime = 0L
    pauseIssues.forEach { (start, end) ->
        totalPauseTime += end - start
    }
    val penalty = totalPauseTime / 100
    val pauseScore = (100 - penalty.toLong()).coerceIn(60, 100)

    Log.d("AzurePronunciation", "totalPauseTime=$totalPauseTime, penalty=$penalty, pauseScore=$pauseScore")

    fun formatTime(tick: Long): String {
        val ms = tick / 10_000 // 100ns -> ms
        val minutes = ms / 60000
        val seconds = (ms % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun parseToTimeRangeList(list: List<Pair<String, String>>): List<TimeRange> {
        return list.map { (start, end) -> TimeRange(start, end) }
    }

// 이미 있는 리스트들: pauseIssues, speedIssues, ...
    val issueDetails = IssueDetails(
        pauseIssues = parseToTimeRangeList(pauseIssues.map { formatTime(it.first) to formatTime(it.second) }),
        speedIssues = parseToTimeRangeList(speedIssues.map { formatTime(it.first) to formatTime(it.second) }),
        volumeIssues = parseToTimeRangeList(volumeIssues.map { formatTime(it.first) to formatTime(it.second) }),
        pronunciationIssues = parseToTimeRangeList(pronunciationIssues.map { formatTime(it.first) to formatTime(it.second) })
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
        "issueDetails" to issueDetails // ✅ 이제 이건 실제 IssueDetails 객체임
    )

}

/**
 * WAV 파일에서 직접 볼륨 레벨을 측정하는 함수
 */
private fun measureVolumeFromWavFile(file: File): Double {
    try {
        RandomAccessFile(file, "r").use { raf ->
            // WAV 헤더 건너뛰기 (일반적으로 44바이트)
            raf.seek(44)

            val buffer = ByteArray(1024)
            var sum = 0.0
            var count = 0

            while (raf.read(buffer) != -1) {
                for (i in buffer.indices step 2) {
                    if (i + 1 < buffer.size) {
                        // 16비트 리틀 엔디안으로 읽기
                        val sample = (buffer[i + 1].toInt() shl 8) or (buffer[i].toInt() and 0xFF)
                        sum += sample * sample
                        count++
                    }
                }
            }

            if (count > 0) {
                val rms = sqrt(sum / count)
                return 20 * log10(rms / 32768.0) // 16비트 오디오의 최대값으로 정규화
            }
        }
    } catch (e: Exception) {
        Log.e("VolumeAnalysis", "WAV 파일 분석 오류: ${e.message}")
    }

    return -60.0 // 기본값 (매우 조용함)
}

// 볼륨 점수 계산 로직 개선
// 기존: 절대적인 dB 값을 기준으로 채점
// 개선: 마이크 특성을 고려한 상대적 볼륨 평가
// WAV 파일 분석시 RMS 계산 개선
private fun calculateRMS(bytes: ByteArray): Double {
    if (bytes.isEmpty()) return 0.0

    var sum = 0.0
    // 16비트 오디오 데이터로 가정하고 계산
    for (i in bytes.indices step 2) {
        if (i + 1 < bytes.size) {
            val sample = (bytes[i + 1].toInt() shl 8) or (bytes[i].toInt() and 0xFF)
            // 16비트 signed 값의 범위는 -32768 ~ 32767
            val normalizedSample = sample / 32768.0
            sum += normalizedSample * normalizedSample
        }
    }

    return sqrt(sum / (bytes.size / 2))
}