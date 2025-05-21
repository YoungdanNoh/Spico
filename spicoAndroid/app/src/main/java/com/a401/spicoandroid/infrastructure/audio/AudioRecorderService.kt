package com.a401.spicoandroid.infrastructure.audio

import android.Manifest
import android.content.Context
import android.media.*
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and
import kotlin.math.absoluteValue

class AudioRecorderService(private val context: Context) {

    private var isRecording = false
    private var isPaused = false
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    private lateinit var pcmFile: File
    private lateinit var wavFile: File
    private lateinit var outputStream: FileOutputStream

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun start(scope: CoroutineScope, onWaveformUpdate: (List<Float>) -> Unit, onComplete: (File) -> Unit) {
        if (isRecording) return

        cleanupPrevious()

        try {
            prepareFiles()

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )
            audioRecord?.startRecording()

            isRecording = true
            isPaused = false

            recordingJob = scope.launch(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize)

                while (isRecording) {
                    if (isPaused) {
                        delay(50)
                        continue
                    }

                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        processBuffer(buffer, read, onWaveformUpdate)
                    }
                }

                stopInternal()
                writeWavHeader()
                onComplete(wavFile)
            }
        } catch (e: Exception) {
            Log.e("Recorder", "❌ start 실패: ${e.message}", e)
            stop()
        }
    }

    fun pause() {
        if (isRecording) {
            isPaused = true
            Log.d("Recorder", "⏸ 녹음 일시정지")
        }
    }

    fun resume() {
        if (isRecording && isPaused) {
            isPaused = false
            Log.d("Recorder", "▶ 녹음 재개")
        }
    }

    fun stop() {
        if (isRecording) {
            isRecording = false
            isPaused = false
        }
    }

    private fun cleanupPrevious() {
        try {
            recordingJob?.cancel()
            audioRecord?.release()
            if (::outputStream.isInitialized) {
                outputStream.flush()
                outputStream.close()
            }
        } catch (e: Exception) {
            Log.w("Recorder", "이전 상태 정리 중 예외: ${e.message}")
        }

        isRecording = false
        isPaused = false
        audioRecord = null
        recordingJob = null
    }

    private fun prepareFiles() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "AUDIO_$timestamp"
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: context.filesDir

        pcmFile = File(dir, "$fileName.pcm")
        wavFile = File(dir, "$fileName.wav")
        outputStream = FileOutputStream(pcmFile)
    }

    private fun processBuffer(
        buffer: ShortArray,
        read: Int,
        onWaveformUpdate: (List<Float>) -> Unit
    ) {
        val waveform = buffer.take(read)
            .map { it.toFloat().absoluteValue / Short.MAX_VALUE }
        onWaveformUpdate(waveform.takeLast(60))

        val byteBuffer = ByteArray(read * 2)
        for (i in 0 until read) {
            val value = buffer[i]
            byteBuffer[i * 2] = (value and 0x00FF).toByte()
            byteBuffer[i * 2 + 1] = ((value.toInt() shr 8) and 0xFF).toByte()
        }

        outputStream.write(byteBuffer)
    }

    private fun stopInternal() {
        try {
            audioRecord?.stop()
            audioRecord?.release()
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            Log.e("Recorder", "stopInternal 실패: ${e.message}", e)
        } finally {
            audioRecord = null
        }
    }

    private fun writeWavHeader() {
        val pcmSize = pcmFile.length().toInt()
        val channels = 1
        val byteRate = sampleRate * channels * 16 / 8

        val header = ByteArray(44).apply {
            this[0] = 'R'.code.toByte(); this[1] = 'I'.code.toByte()
            this[2] = 'F'.code.toByte(); this[3] = 'F'.code.toByte()
            writeIntLE(4, 36 + pcmSize)
            this[8] = 'W'.code.toByte(); this[9] = 'A'.code.toByte()
            this[10] = 'V'.code.toByte(); this[11] = 'E'.code.toByte()
            this[12] = 'f'.code.toByte(); this[13] = 'm'.code.toByte()
            this[14] = 't'.code.toByte(); this[15] = ' '.code.toByte()
            writeIntLE(16, 16)
            this[20] = 1; this[21] = 0
            this[22] = channels.toByte(); this[23] = 0
            writeIntLE(24, sampleRate)
            writeIntLE(28, byteRate)
            this[32] = (channels * 16 / 8).toByte(); this[33] = 0
            this[34] = 16; this[35] = 0
            this[36] = 'd'.code.toByte(); this[37] = 'a'.code.toByte()
            this[38] = 't'.code.toByte(); this[39] = 'a'.code.toByte()
            writeIntLE(40, pcmSize)
        }

        FileOutputStream(wavFile).use { out ->
            out.write(header)
            FileInputStream(pcmFile).use { it.copyTo(out) }
        }

        MediaScannerConnection.scanFile(
            context,
            arrayOf(wavFile.absolutePath),
            arrayOf("audio/wav")
        ) { path, _ ->
            Log.d("Recorder", "📂 MediaScanner 등록 완료: $path")
        }
    }

    private fun ByteArray.writeIntLE(offset: Int, value: Int) {
        this[offset] = (value and 0xFF).toByte()
        this[offset + 1] = ((value shr 8) and 0xFF).toByte()
        this[offset + 2] = ((value shr 16) and 0xFF).toByte()
        this[offset + 3] = ((value shr 24) and 0xFF).toByte()
    }
}


