package com.a401.spicoandroid.infrastructure.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale
import kotlin.apply
import kotlin.collections.firstOrNull

class SpeechRecognizerHelper(context: Context) {

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var onResultCallback: (String) -> Unit = {}
    private var onErrorCallback : (Int) -> Unit = {}
    private var onNoResultCallback: () -> Unit = {}

    private var userCanceled = false

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN)
    }

    init {
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                if (userCanceled) return

                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()

                if (text != null) onResultCallback(text)
                else onNoResultCallback()
            }

            override fun onError(error: Int) {
                if (userCanceled) return
                onErrorCallback(error)
            }

            override fun onEndOfSpeech() {}
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    /* ---------- public API ---------- */
    fun start() {
        userCanceled = false
        recognizer.startListening(intent)
    }

    fun stop() = recognizer.stopListening()

    fun cancel() {
        if (userCanceled) return
        userCanceled = true
        recognizer.cancel()
    }

    fun destroy() {
        userCanceled = true
        recognizer.destroy()
    }

    /* ---------- 콜백 주입 ---------- */
    fun setOnResult(block: (String) -> Unit) { onResultCallback = block }
    fun setOnError(block: (Int) -> Unit)   { onErrorCallback  = block }
    fun setOnNoResult(block: () -> Unit)    { onNoResultCallback = block }
}


