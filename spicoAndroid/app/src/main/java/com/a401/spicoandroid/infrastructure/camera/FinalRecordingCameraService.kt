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
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import com.a401.spicoandroid.common.utils.FileUtil
import java.text.SimpleDateFormat
import java.util.*
import java.io.File
import java.nio.ByteBuffer

class FinalRecordingCameraService(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private var recording: Recording? = null
    private var videoCapture: VideoCapture<Recorder>? = null

    fun startCamera(onReady: () -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    videoCapture
                )
                onReady()
            } catch (e: Exception) {
                Log.e("CameraX", "Camera bind failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun startRecording(onFinished: (uri: Uri?) -> Unit) {
        val videoCapture = this.videoCapture ?: return

        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Spico")
            }
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        recording = videoCapture.output
            .prepareRecording(context, outputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        context,
                        android.Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(context)) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    val videoUri = event.outputResults.outputUri
                    Log.d("CameraX", "Video saved to: $videoUri")

                    extractAudioFromMp4(
                        context = context,
                        inputUri = videoUri,
                        onSuccess = { wavPath ->
                            Log.d("CameraX", "Audio extracted to: $wavPath")
                            // 필요하면 여기서 WAV로 추가 변환 등 진행 가능
                        },
                        onError = { e ->
                            Log.e("CameraX", "Audio extract error: ${e.message}")
                        }
                    )

                    onFinished(videoUri)
                    recording = null
                }
            }
    }

    fun stopRecording(onFinished: () -> Unit) {
        recording?.stop()
        recording = null
        onFinished()
    }

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

}
