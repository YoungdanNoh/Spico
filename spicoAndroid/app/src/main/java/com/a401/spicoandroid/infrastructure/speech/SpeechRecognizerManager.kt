package com.a401.spicoandroid.infrastructure.speech

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class SpeechRecognizerManager(
    context: Context
) : DefaultLifecycleObserver {

    private val helper = SpeechRecognizerHelper(context)
    private var isCancelled = false

    fun start(
        onResult: (String) -> Unit,
        onErrorToast: (String) -> Unit
    ) {
        isCancelled = false      // 새 세션
        helper.apply {
            /* 결과 */
            setOnResult { text ->
                if (!isCancelled) onResult(text)
            }

            /* 오류 */
            setOnError { _ ->
                if (!isCancelled) {
                    onErrorToast("음성 인식 중 오류가 발생했어요.")
                }
            }

            /* no‑match */
            setOnNoResult {
                if (!isCancelled) {
                    onErrorToast("말씀을 잘 못 알아들었어요.")
                }
            }

            start()
        }
    }

    fun cancel() {
        if (isCancelled) return
        isCancelled = true

        /* 1) 콜백 차단 ─ 재시작 루프 방지 */
        helper.setOnResult   {}   // no‑op
        helper.setOnError    {}   // "
        helper.setOnNoResult {}   // "

        /* 2) 현재 세션 즉시 종료 */
        helper.cancel()
        /* ※ destroy() 는 여기서 호출하지 말고 onDestroy/onCleared 에서 */
    }

    /* ---------- lifecycle hook ---------- */
    override fun onStop(owner: LifecycleOwner)   = cancel()

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        helper.destroy()
    }
}