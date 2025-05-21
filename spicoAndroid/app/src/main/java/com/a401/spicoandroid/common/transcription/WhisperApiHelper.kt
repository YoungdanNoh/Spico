import android.util.Log
import com.a401.spicoandroid.BuildConfig.OPENAI_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

object WhisperApiHelper {

    suspend fun transcribeWavFile(file: File): String {
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                file.asRequestBody("audio/wav".toMediaTypeOrNull())
            )
            .addFormDataPart("model", "whisper-1") // ✅ model 파라미터 추가!
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/audio/transcriptions")
            .addHeader("Authorization", "Bearer ${OPENAI_KEY}") // ✅ 키는 실제 값으로 교체
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Whisper STT 실패: $response")

                val json = JSONObject(response.body?.string() ?: "")
                json.getString("text") // 응답 형식은 {"text": "변환된 텍스트"}
            }
        }
    }
}
