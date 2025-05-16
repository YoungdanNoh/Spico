package com.a401.spicoandroid.infrastructure.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast

class GoogleStt(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening: Boolean = false

    fun start() {
        if (isListening) return // 중복 방지
        isListening = true
        listen()
    }

    fun stop() {
        isListening = false
        stopListening()
    }

    private fun listen(){

        // 이전 인식기 정리
        stop()

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
                   
                   // 데시벨 측정
                   
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
                   if (isListening) listen()
               }

               override fun onResults(results: Bundle?) {
                   Log.d("SpeechRecognizer", "onResults 호출됨")

                   val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                   if (!matches.isNullOrEmpty()) {
                       val resultText = matches[0]

                       Log.d("SpeechRecognizer", "결과: $resultText")
                       onResult(resultText)
                   } else {
                       onError("결과 없음")
                   }

                   if (isListening) listen() // 계속 듣기
               }

               override fun onPartialResults(partialResults: Bundle?) {
               }

               override fun onEvent(eventType: Int, params: Bundle?) {
               }
           })
       }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
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

}