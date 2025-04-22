package com.a401.speakoandroid.common.utils

import com.a401.speakoandroid.common.domain.DataResource
import com.a401.speakoandroid.common.domain.ExceptionHandler

suspend fun <T> safeApiCall(
    block: suspend () -> T
): DataResource<T> {
    return try {
        DataResource.success(block())
    } catch (e: Throwable) {
        DataResource.error(ExceptionHandler.handle(e))
    }
}