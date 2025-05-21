package com.a401.spicoandroid.infrastructure.audio

import android.Manifest
import android.media.*
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class AudioAnalyzer {

    private var isRecording = false
    private var audioRecord: AudioRecord? = null

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun start(scope: CoroutineScope, onUpdate: (List<Float>) -> Unit) {
        val sampleRate = 44100
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true

        scope.launch(Dispatchers.IO) {
            val buffer = ShortArray(bufferSize)
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val raw = buffer.take(read)
                    val debugSample = raw.take(10).joinToString()
                    android.util.Log.d("AudioDebug", "🎙 rawShort: $debugSample")

                    val amplitudes = buffer.take(read).map {
                        val normalized = it.toFloat().absoluteValue / Short.MAX_VALUE
                        val boosted = (normalized * 5f).coerceIn(0f, 1f)
                        boosted
                    }
                    onUpdate(amplitudes.takeLast(60))
                }
            }
        }
    }

    fun stop() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}
