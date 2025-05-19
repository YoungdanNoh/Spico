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
import com.a401.spicoandroid.infrastructure.camera.AudioExtractor.extractAudioFromMp4
import com.a401.spicoandroid.infrastructure.camera.AudioExtractor.m4aToPcmAndConvertToWav
import java.text.SimpleDateFormat
import java.util.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class FinalRecordingCameraService(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val script: String? = null
) {
    private var recording: Recording? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var stopCallback: (() -> Unit)? = null

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
                    onFinished(event.outputResults.outputUri)
                    stopCallback?.invoke() // stopRecording에서 등록한 콜백 실행
                    val videoUri = event.outputResults.outputUri
                    Log.d("CameraX", "Video saved to: $videoUri")

                    extractAudioFromMp4(
                        context = context,
                        inputUri = videoUri,
                        onSuccess = { m4aPath ->
                            Log.d("CameraX", "Audio extracted to: $m4aPath")

                            // m4a -> pcm -> wav
                            m4aToPcmAndConvertToWav(
                                context = context,
                                m4aPath = m4aPath,
                                onSuccess = { wavPath ->
                                    Log.d("CameraX", "Final WAV path: $wavPath")
                                },
                                onError = { e ->
                                    Log.e("CameraX", "WAV convert error: ${e.message}")
                                }
                            )
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
        if (recording == null) {
            onFinished()
        } else {
            stopCallback = {
                onFinished()
                stopCallback = null
            }
            recording?.stop()
        }
    }
}