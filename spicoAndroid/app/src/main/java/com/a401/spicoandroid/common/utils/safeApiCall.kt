package com.a401.spicoandroid.common.utils

import android.util.Log
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.domain.ExceptionHandler

suspend fun <T> safeApiCall(
    block: suspend () -> T
): DataResource<T> {
    return try {
        DataResource.success(block())
    } catch (e: Throwable) {
        Log.e("safeApiCall", "❌ API 실패", e) // 전체 스택트레이스 출력
        val handled = ExceptionHandler.handle(e)
        Log.e("safeApiCall", "📋 처리된 에러 메시지: ${handled.message}")
        DataResource.error(ExceptionHandler.handle(e))
    }
}