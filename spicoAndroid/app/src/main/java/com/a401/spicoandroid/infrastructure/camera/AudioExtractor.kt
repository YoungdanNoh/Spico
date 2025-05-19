package com.a401.spicoandroid.infrastructure.camera

import android.content.ContentValues
import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.a401.spicoandroid.common.utils.FileUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

object AudioExtractor {
    fun extractAudioFromMp4(
        context: Context,
        inputUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        try {
            val inputPath = FileUtil.getPathFromUri(context, inputUri)
                ?: throw IllegalArgumentException("Cannot resolve input file path")

            val outputFile = File(context.cacheDir, "extracted_audio_${System.currentTimeMillis()}.m4a")
            val outputPath = outputFile.absolutePath

            val extractor = MediaExtractor()
            extractor.setDataSource(inputPath)

            val audioTrackIndex = (0 until extractor.trackCount).firstOrNull {
                val format = extractor.getTrackFormat(it)
                format.getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
            } ?: throw IllegalArgumentException("No audio track found in video")

            extractor.selectTrack(audioTrackIndex)
            val format = extractor.getTrackFormat(audioTrackIndex)

            val muxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            val newTrackIndex = muxer.addTrack(format)
            muxer.start()

            val buffer = ByteBuffer.allocate(1024 * 1024)
            val bufferInfo = MediaCodec.BufferInfo()

            while (true) {
                bufferInfo.offset = 0
                bufferInfo.size = extractor.readSampleData(buffer, 0)
                if (bufferInfo.size < 0) break
                bufferInfo.presentationTimeUs = extractor.sampleTime
                bufferInfo.flags = 0
                muxer.writeSampleData(newTrackIndex, buffer, bufferInfo)
                extractor.advance()
            }

            muxer.stop()
            muxer.release()
            extractor.release()

            onSuccess(outputPath)
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }
    }

    fun m4aToPcmAndConvertToWav(
        context: Context,
        m4aPath: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val extractor = MediaExtractor()
            extractor.setDataSource(m4aPath)

            val audioTrackIndex = (0 until extractor.trackCount).firstOrNull {
                extractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
            } ?: throw IllegalArgumentException("No audio track found")

            extractor.selectTrack(audioTrackIndex)
            val format = extractor.getTrackFormat(audioTrackIndex)
            val mime = format.getString(MediaFormat.KEY_MIME) ?: throw IllegalArgumentException("No MIME")

            val decoder = MediaCodec.createDecoderByType(mime)
            decoder.configure(format, null, null, 0)
            decoder.start()

            val pcmFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.pcm")
            val pcmOutStream = FileOutputStream(pcmFile)

            val inputBuffers = decoder.inputBuffers
            val outputBuffers = decoder.outputBuffers
            val bufferInfo = MediaCodec.BufferInfo()

            var sawInputEOS = false
            var sawOutputEOS = false

            while (!sawOutputEOS) {
                if (!sawInputEOS) {
                    val inputBufferIndex = decoder.dequeueInputBuffer(10000)
                    if (inputBufferIndex >= 0) {
                        val inputBuffer = inputBuffers[inputBufferIndex]
                        val sampleSize = extractor.readSampleData(inputBuffer, 0)
                        if (sampleSize < 0) {
                            decoder.queueInputBuffer(
                                inputBufferIndex,
                                0,
                                0,
                                0L,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM
                            )
                            sawInputEOS = true
                        } else {
                            decoder.queueInputBuffer(
                                inputBufferIndex,
                                0,
                                sampleSize,
                                extractor.sampleTime,
                                0
                            )
                            extractor.advance()
                        }
                    }
                }

                val outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 10000)
                if (outputBufferIndex >= 0) {
                    val outputBuffer = outputBuffers[outputBufferIndex]
                    val chunk = ByteArray(bufferInfo.size)
                    outputBuffer.get(chunk)
                    outputBuffer.clear()

                    if (chunk.isNotEmpty()) {
                        pcmOutStream.write(chunk)
                    }

                    decoder.releaseOutputBuffer(outputBufferIndex, false)

                    if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        sawOutputEOS = true
                    }
                }
            }

            decoder.stop()
            decoder.release()
            extractor.release()
            pcmOutStream.flush()
            pcmOutStream.close()

            // WAV 변환
            val wavFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.wav")
            convertPcmToWav(pcmFile, wavFile)

            val savedUri = saveWavToExternalStorage(context, wavFile)
            if (savedUri != null) {
                onSuccess(savedUri.toString())
            } else {
                onError(Exception("Failed to save WAV file to external storage"))
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun convertPcmToWav(pcmFile: File, wavFile: File) {
        val pcmSize = pcmFile.length().toInt()
        val sampleRate = 88200
        val channels = 1
        val byteRate = sampleRate * channels * 16 / 8

        val header = ByteArray(44)
        val totalDataLen = pcmSize + 36
        val totalAudioLen = pcmSize

        // "RIFF"
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        writeInt(header, 4, totalDataLen)
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        writeInt(header, 16, 16)
        writeShort(header, 20, 1.toShort()) // PCM
        writeShort(header, 22, channels.toShort())
        writeInt(header, 24, sampleRate)
        writeInt(header, 28, byteRate)
        writeShort(header, 32, (channels * 16 / 8).toShort())
        writeShort(header, 34, 16.toShort())
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        writeInt(header, 40, totalAudioLen)

        val wavOut = FileOutputStream(wavFile)
        wavOut.write(header)

        val pcmIn = FileInputStream(pcmFile)
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (pcmIn.read(buffer).also { bytesRead = it } != -1) {
            wavOut.write(buffer, 0, bytesRead)
        }

        pcmIn.close()
        wavOut.flush()
        wavOut.close()
    }

    private fun writeInt(buffer: ByteArray, offset: Int, value: Int) {
        buffer[offset] = (value and 0xff).toByte()
        buffer[offset + 1] = ((value shr 8) and 0xff).toByte()
        buffer[offset + 2] = ((value shr 16) and 0xff).toByte()
        buffer[offset + 3] = ((value shr 24) and 0xff).toByte()
    }

    private fun writeShort(buffer: ByteArray, offset: Int, value: Short) {
        buffer[offset] = (value.toInt() and 0xff).toByte()
        buffer[offset + 1] = ((value.toInt() shr 8) and 0xff).toByte()
    }

    fun saveWavToExternalStorage(context: Context, wavFile: File): Uri? {
        val filename = "spico_audio_${System.currentTimeMillis()}.wav"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Spico")  // Music/Spico 폴더에 저장
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(wavFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return uri
    }
}
