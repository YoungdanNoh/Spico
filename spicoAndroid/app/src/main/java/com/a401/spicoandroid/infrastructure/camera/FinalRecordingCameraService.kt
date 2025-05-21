package com.a401.spicoandroid.infrastructure.camera

import android.content.ContentValues
import android.content.Context
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
import com.a401.spicoandroid.domain.finalmode.model.AssessmentResult
import com.a401.spicoandroid.domain.finalmode.model.IssueDetails
import com.a401.spicoandroid.domain.finalmode.model.TimeRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class FinalRecordingCameraService(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val script: String? = null,
    private val isQuestionMode: Boolean = false,
    private val setAssessmentResult: ((AssessmentResult) -> Unit)? = null,
    private val onSttResult: ((questionId: Int, transcript: String) -> Unit)? = null
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

    fun startRecording(
        projectId: Int,
        practiceId: Int,
        questionId: Int? = null,
        fileTag: String = "", // 예: "", "qna1", "qna2"
        onFinished: (uri: Uri?) -> Unit
    ) {
        val videoCapture = this.videoCapture ?: return

        val name = "finalmode_${fileTag}_p${projectId}_r${practiceId}"

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

                    CoroutineScope(Dispatchers.IO).launch {
                            val wavUri = Media3AudioExtractor.extractAudioFromMp4(
                                context = context,
                                inputVideoUri = videoUri,
                                outputFileName = "${name}_audio.wav"
                            )
                            Log.d("CameraX", "Audio extracted to WAV: $wavUri")
                            val wavFile = uriToFile(context, wavUri)

                        if (isQuestionMode) {
                                // ✅ Whisper STT
                                Log.d("CameraX", "STT 처리 시작: ${name}")
                                val transcript = WhisperApiHelper.transcribeWavFile(wavFile)
                                Log.d("CameraX", "STT 결과: $transcript")

                                questionId?.let {
                                    onSttResult?.invoke(questionId, transcript)
                                    Log.d("CameraX", "STT 결과 전달 완료")
                                }
                        } else {
                            // ✅ Azure 발음 평가
                            val resultMap: Map<String, Any> = pronunciationAssessmentContinuousWithFile(
                                context, wavFile, script ?: ""
                            )

                            Log.d("AzurePronunciation", "resultMap: $resultMap")

                            val result = mapToAssessmentResult(resultMap)
//                            val result = AssessmentResult(
//                                transcribedText = resultMap["transcribedText"] as? String ?: "",
//                                accuracyScore = resultMap["accuracyScore"] as? Double ?: 0.0,
//                                completenessScore = resultMap["completenessScore"] as? Double ?: 0.0,
//                                fluencyScore = resultMap["fluencyScore"] as? Double ?: 0.0,
//                                pronunciationScore = resultMap["pronunciationScore"] as? Double ?: 0.0,
//                                pauseScore = resultMap["pauseScore"] as? Double ?: 0.0,
//                                volumeScore = resultMap["volumeScore"] as? Double ?: 0.0,
//                                speedScore = resultMap["speedScore"] as? Double ?: 0.0,
//                                issueDetails = (resultMap["issueDetails"] as? Map<String, List<Pair<String, String>>>)
//                                    ?.let { details ->
//                                        IssueDetails(
//                                            pauseIssues = details["pauseIssues"].orEmpty().map { TimeRange(it.first, it.second) },
//                                            speedIssues = details["speedIssues"].orEmpty().map { TimeRange(it.first, it.second) },
//                                            volumeIssues = details["volumeIssues"].orEmpty().map { TimeRange(it.first, it.second) },
//                                            pronunciationIssues = details["pronunciationIssues"].orEmpty().map { TimeRange(it.first, it.second) }
//                                        )
//                                    } ?: IssueDetails(emptyList(), emptyList(), emptyList(), emptyList())
//                            )
                            setAssessmentResult?.invoke(result)
                        }
                    }

                    onFinished(videoUri)
                    recording = null
                }
            }
    }

    fun stopRecording(onFinished: () -> Unit) {
        stopCallback = {
            onFinished()
            stopCallback = null
        }

        if (recording == null) {
            stopCallback?.invoke()
        } else {
            recording?.stop()
        }
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream from URI: $uri")

        val tempFile = File(context.cacheDir, "temp_audio.wav")
        val outputStream = FileOutputStream(tempFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    fun mapToAssessmentResult(resultMap: Map<String, Any>): AssessmentResult {
        return AssessmentResult(
            transcribedText = resultMap["transcribedText"] as String,
            accuracyScore = (resultMap["accuracyScore"] as Number).toDouble(),
            completenessScore = (resultMap["completenessScore"] as Number).toDouble(),
            fluencyScore = (resultMap["fluencyScore"] as Number).toDouble(),
            pronunciationScore = (resultMap["pronunciationScore"] as Number).toDouble(),
            pauseScore = (resultMap["pauseScore"] as Number).toDouble(),
            volumeScore = (resultMap["volumeScore"] as Number).toDouble(),
            speedScore = (resultMap["speedScore"] as Number).toDouble(),
            issueDetails = resultMap["issueDetails"] as IssueDetails
        )
    }
}
