package com.a401.spicoandroid.common.data.dto

data class ApiResponse<T>(
    val success: Boolean,
    val errorMsg: String?,
    val errorCode: String?,
    val data: T?
)

inline fun <T, R> ApiResponse<T>.getOrThrow(mapper: (T) -> R): R {
    if (success && data != null) {
        return mapper(data)
    } else {
        throw Exception(errorMsg ?: "Unknown error (code=$errorCode)")
    }
}

inline fun <T, R> ApiResponse<T>.getOrThrowNull(mapper: (T?) -> R): R {
    if (success) {
        return mapper(data)
    } else {
        throw Exception(errorMsg ?: "Unknown error (code=$errorCode)")
    }
}