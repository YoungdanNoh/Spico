package com.a401.spicoandroid.infrastructure.camera

import android.content.ContentValues
import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

object Media3AudioExtractor {
    suspend fun extractAudioFromMp4(
        context: Context,
        inputVideoUri: Uri,
        outputFileName: String? = null
    ): Uri = withContext(Dispatchers.IO) {

        val fileName = outputFileName ?: "extracted_audio_${System.currentTimeMillis()}.wav"

        // 출력 URI 생성
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Spico")
            }
        }

        val outputUri = context.contentResolver.insert(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw Exception("Failed to create output URI")

        try {
            // MediaExtractor 사용
            val extractor = MediaExtractor()
            context.contentResolver.openFileDescriptor(inputVideoUri, "r")?.use { pfd ->
                extractor.setDataSource(pfd.fileDescriptor)

                // 오디오 트랙 찾기
                var audioTrackIndex = -1
                var audioFormat: MediaFormat? = null

                for (i in 0 until extractor.trackCount) {
                    val format = extractor.getTrackFormat(i)
                    val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
                    if (mime.startsWith("audio/")) {
                        audioTrackIndex = i
                        audioFormat = format
                        break
                    }
                }

                if (audioTrackIndex == -1 || audioFormat == null) {
                    throw Exception("No audio track found in video")
                }

                // 오디오 트랙 선택
                extractor.selectTrack(audioTrackIndex)

                // WAV 파일로 변환 및 저장
                context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                    convertToWav(extractor, audioFormat, outputStream)
                }

                extractor.release()
            }

            Log.d("DirectAudioExtractor", "Audio extracted successfully: $outputUri")
            return@withContext outputUri

        } catch (e: Exception) {
            // 실패 시 생성한 URI 삭제
            context.contentResolver.delete(outputUri, null, null)
            throw e
        }
    }

    /**
     * 추출된 오디오 데이터를 WAV 포맷으로 변환
     */
    private fun convertToWav(
        extractor: MediaExtractor,
        audioFormat: MediaFormat,
        outputStream: OutputStream
    ) {
        val mime = audioFormat.getString(MediaFormat.KEY_MIME) ?: return
        val codec = MediaCodec.createDecoderByType(mime)
        codec.configure(audioFormat, null, null, 0)
        codec.start()

        val inputBuffers = codec.inputBuffers
        val outputBuffers = codec.outputBuffers
        val bufferInfo = MediaCodec.BufferInfo()

        val pcmOutput = ByteArrayOutputStream()

        var sawInputEOS = false
        var sawOutputEOS = false

        while (!sawOutputEOS) {
            if (!sawInputEOS) {
                val inputBufferIndex = codec.dequeueInputBuffer(10000)
                if (inputBufferIndex >= 0) {
                    val inputBuffer = inputBuffers[inputBufferIndex]
                    val sampleSize = extractor.readSampleData(inputBuffer, 0)

                    if (sampleSize < 0) {
                        codec.queueInputBuffer(
                            inputBufferIndex,
                            0,
                            0,
                            0L,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                        sawInputEOS = true
                    } else {
                        val presentationTimeUs = extractor.sampleTime
                        codec.queueInputBuffer(
                            inputBufferIndex,
                            0,
                            sampleSize,
                            presentationTimeUs,
                            0
                        )
                        extractor.advance()
                    }
                }
            }

            val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
            if (outputBufferIndex >= 0) {
                val outputBuffer = outputBuffers[outputBufferIndex]
                val pcmChunk = ByteArray(bufferInfo.size)
                outputBuffer.get(pcmChunk)
                outputBuffer.clear()

                pcmOutput.write(pcmChunk)

                codec.releaseOutputBuffer(outputBufferIndex, false)

                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    sawOutputEOS = true
                }
            }
        }

        codec.stop()
        codec.release()

        val pcmData = pcmOutput.toByteArray()
        val sampleRate = audioFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        val channelCount = audioFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        val bitDepth = 16

        writeWavHeader(outputStream, pcmData.size, sampleRate, channelCount, bitDepth)
        outputStream.write(pcmData)
        outputStream.flush()
    }

    /**
     * WAV 파일 헤더 작성
     */
    private fun writeWavHeader(
        outputStream: OutputStream,
        dataSize: Int,
        sampleRate: Int,
        channelCount: Int,
        bitDepth: Int
    ) {
        val byteRate = sampleRate * channelCount * bitDepth / 8
        val blockAlign = channelCount * bitDepth / 8
        val totalSize = dataSize + 36

        val header = ByteBuffer.allocate(44).apply {
            order(ByteOrder.LITTLE_ENDIAN)

            // RIFF 헤더
            put("RIFF".toByteArray())
            putInt(totalSize)
            put("WAVE".toByteArray())

            // fmt 청크
            put("fmt ".toByteArray())
            putInt(16) // fmt 청크 크기
            putShort(1) // PCM 포맷
            putShort(channelCount.toShort())
            putInt(sampleRate)
            putInt(byteRate)
            putShort(blockAlign.toShort())
            putShort(bitDepth.toShort())

            // data 청크
            put("data".toByteArray())
            putInt(dataSize)
        }

        outputStream.write(header.array())
    }

    /**
     * 오디오 메타데이터 가져오기
     */
    fun getAudioInfo(context: Context, uri: Uri): AudioInfo? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.use {
                it.setDataSource(context, uri)
                val duration = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                val bitrate = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toIntOrNull()

                AudioInfo(duration = duration, bitrate = bitrate)
            }
        } catch (e: Exception) {
            Log.e("DirectAudioExtractor", "Failed to get audio info", e)
            null
        }
    }

    data class AudioInfo(
        val duration: Long?,
        val bitrate: Int?
    )
}