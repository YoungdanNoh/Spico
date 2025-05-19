package com.a401.spicoandroid.infrastructure.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.a401.spicoandroid.infrastructure.speech.model.SpeedType
import com.a401.spicoandroid.infrastructure.speech.model.VolumeLevel
import com.a401.spicoandroid.infrastructure.speech.model.VolumeRecord

class GoogleStt(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening: Boolean = false

    private var speechStartTimestamp: Long = 0L
    private var lastRecordTime: Long = 0L
    private val volumeBuffer = mutableListOf<Float>()
    private val volumeRecords = mutableListOf<VolumeRecord>()
    private var currentVolumeLevel: VolumeLevel? = null
    private var currentStartTime: String? = null

    private val recentAvgList = mutableListOf<Float>()
    private val maxAvgWindowSize = 5  // 최근 5초 정도 관찰

    /* 발표 속도 점수 관련 필드 */
    private var totalCharCount = 0
    private var speechCharCheckpoints = mutableListOf<Int>()
    private var lastSpeedCheckTime: Long = 0L

    /* 휴지 횟수 관련 필드 */
    private var isInPause: Boolean = false
    private var silenceStartTime: Long? = null
    private var pauseCount: Int = 0

    fun start() {
        if (isListening) return // 중복 방지
        isListening = true
        clearAll()
        speechStartTimestamp = System.currentTimeMillis() // 시작 기준점
        listen()
    }

    fun stop() {
        isListening = false
        finalizeVolumeRecord()
        stopListening()
    }

    private fun listen(){

        // 이전 인식기 정리
        stopListening()
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("이 기기에서 음성 인식을 사용할 수 없습니다.")
            return
        }

       speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
           setRecognitionListener(object : RecognitionListener{

               override fun onReadyForSpeech(params: Bundle?) {
                   Log.d("SpeechRecognizer", "음성 인식 준비됨")
               }

               override fun onBeginningOfSpeech() {
                   Log.d("SpeechRecognizer", "사용자가 말하기 시작")
               }

               override fun onRmsChanged(rmsdB: Float) {
                   onWaveformUpdate?.invoke(rmsdB)

                   // 성량 피드백
                   val currentTime = System.currentTimeMillis()
                   volumeBuffer.add(rmsdB)

                   // 1초 주기 측정
                   if (currentTime - lastRecordTime >= 1000) {

                       lastRecordTime = currentTime
                       val avg = volumeBuffer.average().toFloat()
                       volumeBuffer.clear()

                       Log.d("SpeechRecognizer", "avg: ${avg}")


                       recentAvgList.add(avg)
                       if (recentAvgList.size > maxAvgWindowSize) {
                           recentAvgList.removeAt(0)
                       }

                       if (recentAvgList.size >= 2) {
                           val firstHalf = recentAvgList.take(recentAvgList.size / 2).average().toFloat()
                           val secondHalf = recentAvgList.takeLast(recentAvgList.size / 2).average().toFloat()
                           val trend = secondHalf - firstHalf
                           val trendLevel = classifyVolume(trend)

                           val feedback = when (trendLevel) {
                               VolumeLevel.LOUD -> "목소리가 커요!"
                               VolumeLevel.QUIET -> "조금 더 크게 말해볼까요?"
                               VolumeLevel.MIDDLE -> "좋아요! 지금 톤을 유지해요"
                           }
                           onVolumeFeedback?.invoke(feedback)

                           val timeOffset = currentTime - speechStartTimestamp
                           val timeStr = formatElapsedTime(timeOffset)

                           if (currentVolumeLevel == null || currentVolumeLevel != trendLevel) {
                               currentStartTime?.let { start ->
                                   currentVolumeLevel?.let { level ->
                                       volumeRecords.add(
                                           VolumeRecord(
                                               startTime = start,
                                               endTime = timeStr,
                                               volumeLevel = level
                                           )
                                       )
                                   }
                               }
                               currentVolumeLevel = trendLevel
                               currentStartTime = timeStr

                               recentAvgList.clear()
                           } else {
                               volumeRecords.lastOrNull()?.endTime = timeStr
                           }
                       }
                   }

                   /* 휴지 구간 감지 로직 */
                   Log.d("RMS_DEBUG", "rmsdB: $rmsdB")
                   if (rmsdB < 5.0f) {
                       if (!isInPause) {
                           if (silenceStartTime == null) {
                               silenceStartTime = currentTime

                           } else if (currentTime - silenceStartTime!! >= 3000L) {
                               pauseCount++
                               isInPause = true // 휴지 감지 상태 진입
                               silenceStartTime = null // 다시 감지되지 않도록 리셋
                               Log.d("Pause", "휴지 구간 감지됨! 현재 누적: $pauseCount")

                           }
                       }
                   } else {
                       silenceStartTime = null // 음성 다시 시작되면 초기화
                       isInPause = false
                   }

               }

               override fun onBufferReceived(buffer: ByteArray?) {
                   // 녹음하기
               }

               override fun onEndOfSpeech() {
               }

               override fun onError(error: Int) {
                   val message = getErrorMessage(error)
                   Log.e("SpeechRecognizer", "에러 발생: $message")
                   onError(message)

                   finalizeVolumeRecord()

                   if (isListening) listen()
               }

               override fun onResults(results: Bundle?) {
                   Log.d("SpeechRecognizer", "onResults 호출됨")

                   val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                   if (!matches.isNullOrEmpty()) {
                       val resultText = matches[0]

                       Log.d("SpeechRecognizer", "결과: $resultText")
                       onResult(resultText)

                       /* 속도 측정용 */
                       val currentTime = System.currentTimeMillis()
                       totalCharCount += resultText.length // 현재까지 stt된 텍스트의 길이

                       // 1분 경과 시마다 기록
                       if (lastSpeedCheckTime == 0L) {
                           lastSpeedCheckTime = currentTime
                           speechCharCheckpoints.add(totalCharCount)
                       } else if (currentTime - lastSpeedCheckTime >= 60_000L) {
                           speechCharCheckpoints.add(totalCharCount)
                           lastSpeedCheckTime = currentTime
                       }

                   } else {
                       onError("결과 없음")
                   }

                   if (isListening) listen() // 계속 듣기
               }

               override fun onPartialResults(partialResults: Bundle?) {
                   val interim = partialResults
                       ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                       ?.firstOrNull()

                   if (!interim.isNullOrEmpty()) {
                       Log.d("SpeechRecognizer", "🔄 partial: $interim")
                       onPartialResult?.invoke(interim)
                   }
               }

               override fun onEvent(eventType: Int, params: Bundle?) {
               }
           })
       }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        speechRecognizer?.startListening(intent)

    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    fun clearAll() {
        volumeRecords.clear()
        volumeBuffer.clear()
        recentAvgList.clear()
        currentVolumeLevel = null
        currentStartTime = null
        lastRecordTime = 0L

        /* 발표 속도 관련 변수 초기화 */
        totalCharCount = 0
        speechCharCheckpoints.clear()
        lastSpeedCheckTime = 0L

        /* 휴지 횟수 관련 변수 초기화 */
        pauseCount = 0
        silenceStartTime = null
        isInPause = false
    }

    private fun getErrorMessage(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "오디오 녹음 에러"
            SpeechRecognizer.ERROR_CLIENT -> "클라이언트 측 에러"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "권한 없음"
            SpeechRecognizer.ERROR_NETWORK -> "네트워크 오류"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 타임아웃"
            SpeechRecognizer.ERROR_NO_MATCH -> "일치하는 음성 없음"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "음성 인식기가 바쁨"
            SpeechRecognizer.ERROR_SERVER -> "서버 에러"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말을 안 해서 시간 초과"
            else -> "알 수 없는 오류 ($errorCode)"
        }
    }

    private fun classifyVolume(trend: Float): VolumeLevel {
        return when {
            trend > 6.0f -> VolumeLevel.LOUD
            trend < -2.0f -> VolumeLevel.QUIET
            else -> VolumeLevel.MIDDLE
        }
    }

    fun getVolumeRecordsJson(): String {
        val jsonArray = org.json.JSONArray()
        for (record in volumeRecords) {
            val obj = org.json.JSONObject()
            obj.put("startTime", record.startTime)
            obj.put("endTime", record.endTime)
            obj.put("volumeLevel", record.volumeLevel.name)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    fun finalizeVolumeRecord() {
        if (volumeBuffer.isNotEmpty()) {
            val avg = volumeBuffer.average().toFloat()
            volumeBuffer.clear()

            // 평균값 기록
            recentAvgList.add(avg)
            if (recentAvgList.size > maxAvgWindowSize) {
                recentAvgList.removeAt(0)
            }

            // 추세 기반 레벨 분류
            val level = if (recentAvgList.size >= 2) {
                val firstHalf = recentAvgList.take(recentAvgList.size / 2).average().toFloat()
                val secondHalf = recentAvgList.takeLast(recentAvgList.size / 2).average().toFloat()
                val trend = secondHalf - firstHalf
                classifyVolume(trend)
            } else {
                VolumeLevel.MIDDLE
            }

            val timeOffset = System.currentTimeMillis() - speechStartTimestamp
            val timeStr = formatElapsedTime(timeOffset)

            if (currentVolumeLevel == null || currentVolumeLevel != level) {
                // 이전 기록 종료 및 새 레코드 추가
                currentStartTime?.let { start ->
                    currentVolumeLevel?.let { prevLevel ->
                        volumeRecords.add(
                            VolumeRecord(
                                startTime = start,
                                endTime = timeStr,
                                volumeLevel = prevLevel
                            )
                        )
                    }
                }

                // 상태 갱신
                currentVolumeLevel = level
                currentStartTime = timeStr

                recentAvgList.clear()
            } else {
                // 동일한 레벨 → 마지막 레코드 endTime만 갱신
                volumeRecords.lastOrNull()?.endTime = timeStr
            }

            // 종료 시 상태 초기화
            currentStartTime = null
            currentVolumeLevel = null
        }
    }

    private fun formatElapsedTime(elapsedMillis: Long): String {
        val totalSeconds = elapsedMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getVolumeRecordList(): List<VolumeRecord> {
        return volumeRecords.toList()
    }

    private var onWaveformUpdate: ((Float) -> Unit)? = null
    fun setOnWaveformUpdate(callback: (Float) -> Unit) {
        onWaveformUpdate = callback
    }

    private var onVolumeFeedback: ((String) -> Unit)? = null
    fun setOnVolumeFeedback(callback: (String) -> Unit) {
        onVolumeFeedback = callback
    }

    /* 발표 속도 측정 함수 */
    fun getOverallSpeed(): SpeedType {
        if (speechCharCheckpoints.size < 2) return SpeedType.MIDDLE

        Log.d("speed", speechCharCheckpoints.toString())

        // 각 1분 단위 구간의 글자 수 차이 계산
        val countsPerMinute = speechCharCheckpoints.zipWithNext { a, b -> b - a }

        val results = countsPerMinute.map { count ->
            when {
                count < 240 -> SpeedType.SLOW
                count > 280 -> SpeedType.FAST
                else -> SpeedType.MIDDLE
            }
        }

        // 최빈값 반환
        return results.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: SpeedType.MIDDLE
    }

    /* 휴지 횟수를 불러오기 위한 함수 */
    fun getPauseCount(): Int {
        return pauseCount
    }

    private var onPartialResult: ((String) -> Unit)? = null
    fun setOnPartialResult(callback: (String) -> Unit) {
        onPartialResult = callback
    }

}